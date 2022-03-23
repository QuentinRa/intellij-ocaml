package com.or.lang.core;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.or.ide.files.FileBase;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiModule;
import com.or.lang.core.psi.PsiQualifiedPathElement;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.psi.impl.PsiUpperIdentifier;
import com.or.lang.core.psi.reference.PsiUpperSymbolReference;
import com.or.lang.core.type.ORCompositeType;
import com.or.utils.Joiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ORUtil {

    private ORUtil() {
    }

    @NotNull
    public static String fileNameToModuleName(@NotNull String filename) {
        String nameWithoutExtension = FileUtilRt.getNameWithoutExtension(filename);
        if (nameWithoutExtension.isEmpty()) {
            return "";
        }
        return nameWithoutExtension.substring(0, 1).toUpperCase(Locale.getDefault())
                + nameWithoutExtension.substring(1);
    }

    @Nullable
    public static PsiElement prevSibling(@NotNull PsiElement element) {
        // previous sibling without considering whitespace
        PsiElement prevSibling = element.getPrevSibling();
        while (prevSibling != null) {
            ASTNode prevNode = prevSibling.getNode();
            if (prevNode == null || prevNode.getElementType() != TokenType.WHITE_SPACE) {
                break;
            }
            prevSibling = prevSibling.getPrevSibling();
        }
        return prevSibling;
    }

    public static @Nullable PsiElement nextSiblingWithTokenType(@NotNull PsiElement root, @NotNull IElementType elementType) {
        PsiElement found = null;

        PsiElement sibling = root.getNextSibling();
        while (sibling != null) {
            if (sibling.getNode().getElementType() == elementType) {
                found = sibling;
                sibling = null;
            } else {
                sibling = sibling.getNextSibling();
            }
        }

        return found;
    }

    public static @Nullable PsiElement nextSiblingWithTokenType(@NotNull PsiElement root, @NotNull ORCompositeType elementType) {
        return nextSiblingWithTokenType(root, (IElementType) elementType);
    }

    public static @NotNull String getTextUntilTokenType(@NotNull PsiElement root, @Nullable IElementType elementType) {
        StringBuilder text = new StringBuilder(root.getText());

        PsiElement sibling = root.getNextSibling();
        while (sibling != null) {
            if (sibling.getNode().getElementType() == elementType) {
                sibling = null;
            } else {
                text.append(sibling.getText());
                sibling = sibling.getNextSibling();
            }
        }

        return text.toString().trim();
    }

    public static @NotNull String getTextUntilClass(@NotNull PsiElement root, @Nullable Class<?> clazz) {
        StringBuilder text = new StringBuilder(root.getText());

        PsiElement sibling = root.getNextSibling();
        while (sibling != null) {
            if (clazz != null && sibling.getClass().isAssignableFrom(clazz)) {
                sibling = null;
            } else {
                text.append(sibling.getText());
                sibling = sibling.getNextSibling();
            }
        }

        return text.toString().trim();
    }

    public static @NotNull String getTextUntilWhitespace(@NotNull PsiElement root) {
        StringBuilder text = new StringBuilder(root.getText());

        PsiElement sibling = root.getNextSibling();
        while (sibling != null) {
            if (sibling instanceof PsiWhiteSpace) {
                sibling = null;
            } else {
                text.append(sibling.getText());
                sibling = sibling.getNextSibling();
            }
        }

        return text.toString().trim();
    }

    @NotNull
    public static ASTNode nextSiblingNode(@NotNull ASTNode node) {
        ASTNode nextSibling = node.getTreeNext();
        while (nextSibling.getElementType() == TokenType.WHITE_SPACE) {
            nextSibling = nextSibling.getTreeNext();
        }
        return nextSibling;
    }

    @Nullable
    public static PsiElement nextSibling(@Nullable PsiElement element) {
        if (element == null) {
            return null;
        }

        PsiElement nextSibling = element.getNextSibling();
        while ((nextSibling instanceof PsiWhiteSpace)) {
            nextSibling = nextSibling.getNextSibling();
        }

        return nextSibling;
    }

    public static @NotNull <T extends PsiElement> List<T> findImmediateChildrenOfClass(@Nullable PsiElement element, @NotNull Class<T> clazz) {
        if (element == null) {
            return Collections.emptyList();
        }

        return PsiTreeUtil.getStubChildrenOfTypeAsList(element, clazz);
    }

    @NotNull
    public static List<PsiElement> findImmediateChildrenOfType(@Nullable PsiElement element, @NotNull IElementType elementType) {
        PsiElement child = element == null ? null : element.getFirstChild();
        if (child == null) {
            return Collections.emptyList();
        }

        List<PsiElement> result = new ArrayList<>();

        while (child != null) {
            if (child.getNode().getElementType() == elementType) {
                result.add(child);
            }
            child = child.getNextSibling();
        }

        return result;
    }

    @Nullable
    public static PsiElement findImmediateFirstChildOfType(@NotNull PsiElement element, @NotNull IElementType elementType) {
        Collection<PsiElement> children = findImmediateChildrenOfType(element, elementType);
        return children.isEmpty() ? null : children.iterator().next();
    }

    @Nullable
    public static PsiElement findImmediateFirstChildOfType(
            @NotNull PsiElement element, @NotNull ORCompositeType elementType) {
        return findImmediateFirstChildOfType(element, (IElementType) elementType);
    }

    @Nullable
    public static <T extends PsiElement> T findImmediateFirstChildOfClass(@Nullable PsiElement element, @NotNull Class<T> clazz) {
        PsiElement child = element == null ? null : element.getFirstChild();

        while (child != null) {
            if (clazz.isInstance(child)) {
                return clazz.cast(child);
            }
            child = child.getNextSibling();
        }

        return null;
    }

    @Nullable
    public static <T extends PsiElement> T findImmediateLastChildOfClass(@Nullable PsiElement element, @NotNull Class<T> clazz) {
        PsiElement child = element == null ? null : element.getFirstChild();
        T found = null;

        while (child != null) {
            if (clazz.isInstance(child)) {
                //noinspection unchecked
                found = (T) child;
            }
            child = child.getNextSibling();
        }

        return found;
    }

    public static @Nullable PsiElement findImmediateFirstChildOfAnyClass(@NotNull PsiElement element, Class<?> @NotNull ... clazz) {
        PsiElement child = element.getFirstChild();

        while (child != null) {
            if (!(child instanceof PsiWhiteSpace)) {
                for (Class<?> aClazz : clazz) {
                    if (aClazz.isInstance(child)) {
                        return child;
                    }
                }
            }
            child = child.getNextSibling();
        }

        return null;
    }

    public static String @Nullable [] getQualifiedPath(@NotNull PsiElement element) {
        String path = "";

        PsiElement parent = element.getParent();
        while (parent != null) {
            if (parent instanceof PsiQualifiedPathElement) {
                if (parent instanceof PsiNameIdentifierOwner && ((PsiNameIdentifierOwner) parent).getNameIdentifier() == element) {
                    return ((PsiQualifiedPathElement) parent).getPath();
                }
                return (((PsiQualifiedNamedElement) parent).getQualifiedName() + (path.isEmpty() ? "" : "." + path)).split("\\.");
            } else {
                if (parent instanceof PsiNameIdentifierOwner) {
                    String parentName = ((PsiNamedElement) parent).getName();
                    if (parentName != null && !parentName.isEmpty()) {
                        path = parentName + (path.isEmpty() ? "" : "." + path);
                    }
                }
                parent = parent.getParent();
            }
        }

        try {
            PsiFile containingFile = element.getContainingFile(); // Fail in 2019.2... ?
            return (((FileBase) containingFile).getModuleName() + (path.isEmpty() ? "" : "." + path)).split("\\.");
        } catch (PsiInvalidElementAccessException e) {
            return path.split("\\.");
        }
    }

    @NotNull
    public static String getQualifiedName(@NotNull PsiNamedElement element) {
        String name = element.getName();
        String qualifiedPath = Joiner.join(".", getQualifiedPath(element));
        return name == null ? qualifiedPath + ".UNKNOWN" : qualifiedPath + "." + name;
    }

    @Nullable
    public static String computeAlias(@Nullable PsiElement rootElement, boolean lowerAccepted) {
        boolean isALias = true;

        PsiElement currentElement = rootElement;
        StringBuilder aliasName = new StringBuilder();
        IElementType elementType = currentElement == null ? null : currentElement.getNode().getElementType();
        while (elementType != null && elementType != OCamlTypes.SEMI) {
            if (elementType != TokenType.WHITE_SPACE && elementType != OCamlTypes.C_UPPER_SYMBOL && elementType != OCamlTypes.DOT) {
                // if last term is lower symbol, and we accept lower symbol, then it's an alias
                if (elementType != OCamlTypes.C_LOWER_SYMBOL || currentElement.getNextSibling() != null || !lowerAccepted) {
                    isALias = false;
                    break;
                }
            }

            if (elementType != TokenType.WHITE_SPACE) {
                aliasName.append(currentElement.getText());
            }

            currentElement = currentElement.getNextSibling();
            elementType = currentElement == null ? null : currentElement.getNode().getElementType();
        }

        return isALias ? aliasName.toString() : null;
    }

    public static String @Nullable [] getQualifiedNameAsPath(@NotNull PsiQualifiedPathElement element) {
        String[] path = element.getPath();
        if (path == null) {
            return null;
        }

        String name = element.getName();
        if (name == null) {
            return path;
        }

        String[] qpath = new String[path.length + 1];
        System.arraycopy(path, 0, qpath, 0, path.length);
        qpath[qpath.length - 1] = name;
        return qpath;
    }

    public static @Nullable PsiElement resolveModuleSymbol(@Nullable PsiUpperSymbol moduleSymbol) {
        PsiUpperSymbolReference reference = moduleSymbol == null ? null : (PsiUpperSymbolReference) moduleSymbol.getReference();
        PsiElement resolvedSymbol = reference == null ? null : reference.resolveInterface();
        return resolvedSymbol instanceof PsiUpperIdentifier ? resolvedSymbol.getParent() : resolvedSymbol;
    }

    public static @Nullable PsiElement getModuleContent(@NotNull PsiModule module) {
        PsiElement body = module.getModuleType();
        return (body == null) ? module.getBody() : body;
    }

    public static @Nullable <T extends PsiNamedElement> T findImmediateNamedChildOfClass(@Nullable PsiElement element, @NotNull Class<T> clazz, @NotNull String name) {
        return ORUtil.findImmediateChildrenOfClass(element, clazz).stream().filter(item -> name.equals(item.getName())).findFirst().orElse(null);
    }


    public static void renameNodeWithLIDENT(@NotNull CompositeTypePsiElement e, String newName) {
        TreeElement firstChildNode = e.getFirstChildNode();
        PsiElement renamed = ORCodeFactory.createLowerSymbol(e.getProject(), newName);
        if (renamed == null) throw new IncorrectOperationException("Can't rename variable to "+newName+".");
        e.getNode().replaceChild(firstChildNode, renamed.getNode());
    }
}
