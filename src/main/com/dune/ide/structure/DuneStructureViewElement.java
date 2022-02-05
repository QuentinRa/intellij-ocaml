package com.dune.ide.structure;

import com.dune.DuneBundle;
import com.dune.ide.files.DuneFile;
import com.dune.lang.core.psi.PsiDuneFields;
import com.dune.lang.core.psi.PsiStanza;
import com.dune.lang.core.psi.PsiStructuredElement;
import com.dune.utils.psi.DunePsiTreeUtil;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.PsiIconUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DuneStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
    private final PsiElement m_element;
    private final PsiElement m_viewElement;
    private final int m_level;
    private final boolean m_navigateToViewElement;

    DuneStructureViewElement(@NotNull PsiElement element, int level) {
        this(null, element, false, level);
    }

    private DuneStructureViewElement(@Nullable PsiElement viewElement, @NotNull PsiElement element, boolean navigateToViewElement, int level) {
        m_viewElement = viewElement;
        m_element = element;
        m_navigateToViewElement = navigateToViewElement;
        m_level = level;
    }

    @Override
    public @NotNull Object getValue() {
        return m_viewElement == null ? m_element : m_viewElement;
    }

    int getLevel() {
        return m_level;
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (m_element instanceof NavigationItem) {
            NavigationItem targetElement = (NavigationItem) (m_navigateToViewElement ? m_viewElement : m_element);
            assert targetElement != null;
            targetElement.navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        return m_element instanceof NavigationItem && ((NavigationItem) m_element).canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return m_element instanceof NavigationItem
                && ((NavigationItem) m_element).canNavigateToSource();
    }

    @Override
    public @NotNull String getAlphaSortKey() {
        String name = null;
        PsiElement element = m_viewElement == null ? m_element : m_viewElement;

        if (element instanceof PsiNamedElement) {
            name = ((PsiNamedElement) element).getName();
        }

        return name == null ? "" : name;
    }

    @Override
    public @NotNull ItemPresentation getPresentation() {
        if (m_viewElement != null) {
            return new ItemPresentation() {
                @Override
                public String getPresentableText() {
                    return m_viewElement.getText();
                }

                @Nullable
                @Override
                public String getLocationString() {
                    return m_element instanceof PsiNamedElement ?
                            ((PsiNamedElement) m_element).getName() : "";
                }

                @Nullable
                @Override
                public Icon getIcon(boolean unused) {
                    return PsiIconUtil.getProvidersIcon(m_element, 0);
                }
            };
        }

        if (m_element instanceof NavigationItem) {
            ItemPresentation presentation = ((NavigationItem) m_element).getPresentation();
            if (presentation != null) {
                return presentation;
            }
        }

        return new ItemPresentation() {
            @Override public String getPresentableText() {
                return DuneBundle.message("unknown.presentation.for.element", m_element.getText());
            }

            @Override public @Nullable Icon getIcon(boolean unused) {
                return null;
            }
        };
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        List<TreeElement> treeElements = null;

        if (m_element instanceof DuneFile) {
            treeElements = new ArrayList<>();
            m_element.acceptChildren(new ElementVisitor(treeElements, m_level));
            if (!treeElements.isEmpty()) {
                return treeElements.toArray(new TreeElement[0]);
            }
        } else if (m_element instanceof PsiStanza) {
            treeElements = buildStanzaStructure((PsiStanza) m_element);
        }

        if (treeElements != null && !treeElements.isEmpty()) {
            return treeElements.toArray(new TreeElement[0]);
        }

        return EMPTY_ARRAY;
    }

    private @NotNull List<TreeElement> buildStanzaStructure(@NotNull PsiStanza stanzaElement) {
        List<TreeElement> treeElements = new ArrayList<>();

        PsiElement rootElement = DunePsiTreeUtil.findImmediateFirstChildOfClass(stanzaElement, PsiDuneFields.class);
        if (rootElement != null) {
            rootElement.acceptChildren(new ElementVisitor(treeElements, m_level));
        }

        return treeElements;
    }

    static class ElementVisitor extends PsiElementVisitor {
        private final List<TreeElement> m_treeElements;
        private final int m_elementLevel;

        ElementVisitor(List<TreeElement> elements, int elementLevel) {
            m_treeElements = elements;
            m_elementLevel = elementLevel;
        }

        @Override public void visitElement(@NotNull PsiElement element) {
            if (element instanceof PsiStructuredElement) {
                if (((PsiStructuredElement) element).canBeDisplayed()) {
                    m_treeElements.add(new DuneStructureViewElement(element, m_elementLevel));
                }
            }
        }
    }
}
