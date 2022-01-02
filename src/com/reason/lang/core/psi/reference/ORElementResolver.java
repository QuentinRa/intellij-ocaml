package com.reason.lang.core.psi.reference;

import com.intellij.openapi.*;
import com.intellij.openapi.project.*;
import com.intellij.psi.util.*;
import com.reason.lang.core.psi.*;
import jpsplugin.com.reason.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static java.util.Collections.*;

public class ORElementResolver implements Disposable {
    private static final Log LOG = Log.create("ref");

    private final Project myProject;
    private final CachedValue<Map<String, Set<String[]>>> myCachedIncludeDependencies;

    ORElementResolver(@NotNull Project project) {
        myProject = project;
        CachedValuesManager cachedValuesManager = CachedValuesManager.getManager(project);

        myCachedIncludeDependencies = cachedValuesManager.createCachedValue(() -> {
            Map<String, Set<String[]>> dependencies = new HashMap<>();
            return CachedValueProvider.Result.create(dependencies, PsiModificationTracker.MODIFICATION_COUNT);
        });

    }

    @NotNull Resolutions getComputation() {
        return new ResolutionsImpl();
    }

    @Override
    public void dispose() {
    }

    interface Resolutions {
        void add(@NotNull Collection<? extends PsiQualifiedPathElement> elements, boolean includeSource);

        void addIncludesEquivalence();

        void updateWeight(@Nullable String value, Set<String> alternateNames);

        void udpateTerminalWeight(@NotNull String value);

        void removeUpper();

        void removeIfNotFound(String value, @Nullable Set<String> alternateNames);

        void removeIncomplete();

        @NotNull Collection<PsiQualifiedPathElement> resolvedElements();
    }

    /*
     Note: performance is extremely important
     */
    private class ResolutionsImpl implements Resolutions {
        private final Map<Integer, Integer> myWeightPerLevel = new HashMap<>();
        private final Map<String, Map<String, com.reason.lang.core.psi.reference.Resolution>> myResolutionsPerTopModule = new HashMap<>();

        @Override
        public void add(@NotNull Collection<? extends PsiQualifiedPathElement> elements, boolean includeSource) {
            if (elements.isEmpty()) {
                return;
            }

            for (PsiQualifiedPathElement source : elements) {
                String sourceName = source.getName();
                if (sourceName == null) {
                    continue;
                }

                String sourceQName = source.getQualifiedName();
                String[] sourcePath = source.getPath();
                if (sourcePath != null) {
                    // Add source name to the path in case of modules
                    if (includeSource) {
                        String[] newPath = new String[sourcePath.length + 1];
                        System.arraycopy(sourcePath, 0, newPath, 0, sourcePath.length);
                        newPath[sourcePath.length] = sourceName;
                        sourcePath = newPath;
                    }
                    // Remove type name in case of variants
                    if (source instanceof PsiVariantDeclaration) {
                        String[] newPath = new String[sourcePath.length - 1];
                        System.arraycopy(sourcePath, 0, newPath, 0, newPath.length);
                        sourcePath = newPath;
                    }

                    String first = sourcePath[0];
                    Map<String, Resolution> resolutionsPerQName = myResolutionsPerTopModule.get(first);
                    //noinspection Java8MapApi
                    if (resolutionsPerQName == null) {
                        resolutionsPerQName = new HashMap<>();
                        myResolutionsPerTopModule.put(first, resolutionsPerQName);
                    }

                    // try to find duplicates
                    Resolution resolution = resolutionsPerQName.get(sourceQName);
                    if (resolution == null) {
                        resolutionsPerQName.put(sourceQName, new Resolution(sourcePath, source));
                    } else {
                        resolution.myElements.add(source);
                    }
                } else if (includeSource) {
                    Map<String, Resolution> resolutionsPerQName = myResolutionsPerTopModule.get(sourceName);
                    //noinspection Java8MapApi
                    if (resolutionsPerQName == null) {
                        resolutionsPerQName = new HashMap<>();
                        myResolutionsPerTopModule.put(sourceName, resolutionsPerQName);
                    }

                    // try to find duplicates
                    Resolution resolution = resolutionsPerQName.get(sourceQName);
                    if (resolution == null) {
                        resolutionsPerQName.put(sourceQName, new Resolution(new String[]{sourceName}, source));
                    } else {
                        resolution.myElements.add(source);
                    }
                }
            }
        }

        @Override
        public void addIncludesEquivalence() {
            Map<String, Set<String[]>> cachedIncludes = myCachedIncludeDependencies.getValue();

            List<Resolution> includeResolutions = new ArrayList<>();

            for (Map.Entry<String, Map<String, Resolution>> resolutionPerNameEntry : myResolutionsPerTopModule.entrySet()) {
                Map<String, Resolution> resolutions = resolutionPerNameEntry.getValue();
                for (Map.Entry<String, Resolution> resolutionEntry : resolutions.entrySet()) {
                    String key = resolutionEntry.getKey();
                    int pos = key.lastIndexOf(".");
                    String keyPath = pos < 0 ? key : key.substring(0, pos);
                    findResolutionEquivalence(keyPath, resolutionEntry.getValue(), cachedIncludes, includeResolutions, 0);
                }
            }

            for (Resolution includeResolution : includeResolutions) {
                String topModuleName = includeResolution.getTopModuleName();
                Map<String, Resolution> resolutionsPerQName = myResolutionsPerTopModule.get(topModuleName);
                //noinspection Java8MapApi
                if (resolutionsPerQName == null) {
                    resolutionsPerQName = new HashMap<>();
                    myResolutionsPerTopModule.put(topModuleName, resolutionsPerQName);
                }

                PsiQualifiedPathElement includeElement = includeResolution.myElements.get(0);
                String includeQName = includeResolution.joinPath() + (includeElement instanceof PsiModule ? "" : "." + includeElement.getName());

                // try to find duplicates
                Resolution resolution = resolutionsPerQName.get(includeQName);
                if (resolution == null) {
                    resolutionsPerQName.put(includeQName, includeResolution);
                } else {
                    resolution.myElements.add(includeElement);
                }
            }
        }

        private void findResolutionEquivalence(String path, @NotNull Resolution resolution, @NotNull Map<String, Set<String[]>> cachedIncludes, @NotNull List<Resolution> result, int guard) {
            Set<String[]> includeDeps = cachedIncludes.get(path);
            if (includeDeps != null) {
                //    A.B.C.t
                //       A.B.C ==> A   ::   A.B.C.t => A.t
                for (String[] includeDepPath : includeDeps) {
                    Resolution newResolution = new Resolution(includeDepPath, resolution.myElements);
                    result.add(newResolution);
                    String newPath = Joiner.join(".", includeDepPath);
                    if (20 < guard) {
                        LOG.warn("Too much recursion for " + path);
                        return;
                    }

                    if (newPath.equals(path)) {
                        LOG.info("equivalent path found (#" + guard + "): " + path + ", deps: [" + Joiner.join(",", includeDeps, p -> Joiner.join(".", p)) + "]");
                    } else {
                        findResolutionEquivalence(newPath, newResolution, cachedIncludes, result, guard + 1);
                    }
                }
            } else {
                includeDeps = cachedIncludes.get(resolution.getTopModuleName());
                if (includeDeps != null) {
                    //    Core.Types.Visibility
                    //       Core ==> Css  ::   Core.Types.Visibility.t => Css.Types.Visibility.t
                    for (String[] includeDepPath : includeDeps) {
                        String[] newPath = resolution.augmentPath(includeDepPath);
                        Resolution newResolution = new Resolution(newPath, resolution.myElements);
                        result.add(newResolution);
                    }
                }
            }
        }

        public void updateWeight(@Nullable String value, @Nullable Set<String> alternateNames) {
            Map<Integer, Integer> newWeights = new HashMap<>();

            for (Map<String, com.reason.lang.core.psi.reference.Resolution> topModuleEntry : myResolutionsPerTopModule.values()) {
                for (com.reason.lang.core.psi.reference.Resolution resolution : topModuleEntry.values()) {
                    String name = resolution.getCurrentName();
                    if (name != null) {
                        if (value == null || value.equals(name)) {
                            int level = resolution.myLevel;
                            Integer weight = myWeightPerLevel.get(level);
                            int newWeight = weight == null ? 1 : weight + 1;
                            resolution.updateCurrentWeight(newWeight);
                            newWeights.put(level, newWeight);
                        } else if (alternateNames != null) {
                            for (String alternateName : alternateNames) {
                                if (alternateName.equals(name)) {
                                    int level = resolution.myLevel;
                                    Integer weight = myWeightPerLevel.get(level);
                                    int newWeight = weight == null ? 1 : weight + 1;
                                    resolution.updateCurrentWeight(newWeight);
                                    newWeights.put(level, newWeight);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            myWeightPerLevel.putAll(newWeights);
        }

        public void udpateTerminalWeight(@NotNull String value) {
            for (Map<String, com.reason.lang.core.psi.reference.Resolution> topModuleEntry : myResolutionsPerTopModule.values()) {
                for (com.reason.lang.core.psi.reference.Resolution resolution : topModuleEntry.values()) {
                    String name = resolution.getCurrentName();
                    if (value.equals(name) && resolution.isLastLevel()) {
                        // terminal
                        int level = resolution.myLevel;
                        Integer weight = myWeightPerLevel.get(level);
                        int newWeight = weight == null ? 1 : weight + 1;
                        resolution.updateCurrentWeight(newWeight);
                        myWeightPerLevel.put(level, newWeight);
                    }
                }
            }
        }

        public void removeUpper() {
            for (Map<String, com.reason.lang.core.psi.reference.Resolution> topModuleEntry : myResolutionsPerTopModule.values()) {
                topModuleEntry.values().removeIf(resolution -> {
                    String name = resolution.getCurrentName();
                    return name != null && Character.isUpperCase(name.charAt(0));
                });
            }
        }

        // all resolutions must be complete
        public void removeIfNotFound(@NotNull String value, @Nullable Set<String> alternateNames) {
            for (Map<String, com.reason.lang.core.psi.reference.Resolution> topModuleEntry : myResolutionsPerTopModule.values()) {
                topModuleEntry.values().removeIf(resolution -> {
                    String currentName = resolution.getCurrentName();
                    return !value.equals(currentName) && (alternateNames == null || !alternateNames.contains(currentName));
                });
            }
        }

        public void removeIncomplete() {
            for (Map<String, com.reason.lang.core.psi.reference.Resolution> topModuleEntry : myResolutionsPerTopModule.values()) {
                topModuleEntry.values().removeIf(resolution -> !resolution.myIsComplete);
            }
        }

        public @NotNull Collection<PsiQualifiedPathElement> resolvedElements() {
            List<com.reason.lang.core.psi.reference.Resolution> allResolutions = new ArrayList<>();

            // flatten elements
            for (Map<String, com.reason.lang.core.psi.reference.Resolution> topModuleEntry : myResolutionsPerTopModule.values()) {
                allResolutions.addAll(topModuleEntry.values());
            }

            allResolutions.sort(com.reason.lang.core.psi.reference.Resolution::compareTo);

            return allResolutions.isEmpty() ? emptyList() : allResolutions.get(0).myElements;
        }
    }
}
