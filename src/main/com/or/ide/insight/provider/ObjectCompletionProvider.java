package com.or.ide.insight.provider;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PsiIconUtil;
import com.or.ide.search.index.TypeFqnIndex;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiLet;
import com.or.lang.core.psi.PsiLowerSymbol;
import com.or.lang.core.psi.PsiSignature;
import com.or.lang.core.psi.PsiType;
import com.or.lang.core.psi.impl.PsiJsObject;
import com.or.lang.core.psi.impl.PsiLowerIdentifier;
import com.or.lang.core.psi.impl.PsiObjectField;
import com.or.lang.core.psi.reference.PsiLowerSymbolReference;
import com.or.lang.utils.QNameFinder;
import com.or.lang.utils.QNameFinderFactory;
import com.or.utils.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class ObjectCompletionProvider {
    private static final Log LOG = Log.create("insight.object");

    private ObjectCompletionProvider() {
    }

    public static void addCompletions(@NotNull PsiElement element, @NotNull CompletionResultSet resultSet) {
        LOG.debug("OBJECT expression completion");

        PsiElement separator = PsiTreeUtil.prevVisibleLeaf(element);
        PsiElement previousElement = separator == null ? null : separator.getPrevSibling();

        QNameFinder qnameFinder = QNameFinderFactory.getQNameFinder();

        if (previousElement instanceof PsiLowerSymbol) {
            LOG.debug(" -> lower symbol", previousElement);

            PsiLowerSymbolReference reference = (PsiLowerSymbolReference) previousElement.getReference();
            PsiElement resolvedElement = reference == null ? null : reference.resolveInterface();
            if (LOG.isDebugEnabled()) {
                LOG.debug(" -> resolved to", resolvedElement == null ? null : resolvedElement.getParent());
            }

            if (resolvedElement instanceof PsiLowerIdentifier) {
                Collection<PsiObjectField> fields = getFields(qnameFinder, resolvedElement);

                if (fields == null) {
                    LOG.debug("  -> Not a js object");
                } else {
                    for (PsiObjectField field : fields) {
                        String fieldName = field.getName();
                        resultSet.addElement(LookupElementBuilder.create(fieldName)
                                .withIcon(PsiIconUtil.getProvidersIcon(field, 0)));
                    }
                }
            }
        }

        LOG.debug("  -> Nothing found");
    }

    private static @Nullable Collection<PsiObjectField> getFields(@NotNull QNameFinder qnameFinder, @NotNull PsiElement resolvedElement) {
        PsiElement resolvedParent = resolvedElement.getParent();
        if (resolvedParent instanceof PsiLet) {
            PsiLet let = (PsiLet) resolvedParent;
            if (let.isJsObject()) {
                PsiJsObject jsObject = ORUtil.findImmediateFirstChildOfClass(let.getBinding(), PsiJsObject.class);
                return jsObject == null ? null : jsObject.getFields();
            } else {
                PsiType type = getType(let, qnameFinder);
                if (type != null && type.isJsObject()) {
                    PsiJsObject jsObject = ORUtil.findImmediateFirstChildOfClass(type.getBinding(), PsiJsObject.class);
                    return jsObject == null ? null : jsObject.getFields();
                }
            }
        } else if (resolvedParent instanceof PsiObjectField) {
            PsiObjectField field = (PsiObjectField) resolvedParent;
            PsiElement value = field.getValue();
            if (value instanceof PsiJsObject) {
                return ((PsiJsObject) value).getFields();
            } else {
                // Must be an object defined outside
                PsiLowerSymbol lSymbol = ORUtil.findImmediateLastChildOfClass(field, PsiLowerSymbol.class);
                PsiLowerSymbolReference valueReference = lSymbol == null ? null : (PsiLowerSymbolReference) lSymbol.getReference();
                PsiElement valueResolvedElement = valueReference == null ? null : valueReference.resolveInterface();
                return valueResolvedElement == null ? null : getFields(qnameFinder, valueResolvedElement);
            }
        }
        return null;
    }

    private static @Nullable PsiType getType(@NotNull PsiLet let, @NotNull QNameFinder qnameFinder) {
        GlobalSearchScope scope = GlobalSearchScope.allScope(let.getProject());
        PsiSignature letSignature = let.getSignature();
        if (letSignature != null) {
            LOG.debug("Testing let signature", letSignature.getText());

            Set<String> paths = qnameFinder.extractPotentialPaths(let);
            LOG.debug("  Paths found", paths);

            Project project = let.getProject();
            String signatureName = "." + letSignature.getText();
            for (String path : paths) {
                Collection<PsiType> types = TypeFqnIndex.getElements((path + signatureName).hashCode(), project, scope);
                if (!types.isEmpty()) {
                    PsiType type = types.iterator().next();
                    LOG.debug("  -> Found", type);
                    return type;
                }
            }
        }

        return null;
    }
}
