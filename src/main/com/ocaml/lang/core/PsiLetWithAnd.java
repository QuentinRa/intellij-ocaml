package com.ocaml.lang.core;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import com.ocaml.lang.utils.OCamlPsiUtils;
import com.ocaml.utils.ComputeMethod;
import com.ocaml.utils.MayNeedToBeTested;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiStructuredElement;
import com.or.lang.core.psi.impl.PsiLetImpl;
import com.or.lang.core.type.ORTokenElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Statements such as "let x = 5 and y = 5" were not sent correctly
 * to the REPL. This class is making sure that such statements are sent
 * in one instruction.
 */
@MayNeedToBeTested(note = "Tested by the class using this class for now.")
public class PsiLetWithAnd extends PsiLetImpl {

    // wrapper around the core
    private final @NotNull PsiElement core;
    // new values
    private final @NotNull TextRange range;
    private final @NotNull String text;

    public PsiLetWithAnd(@NotNull PsiElement core) {
        super(new CompositePsiElement(new ORTokenElementType("PsiLetWithAnd")) {});
        this.core = core;

        Pair<TextRange, String> values = new Pair<>(core.getTextRange(), core.getText());

        PsiElement res = OCamlPsiUtils.skipMeaninglessNextSibling(core);
        boolean isPrev = false;
        if (res == null || !(res.getText().equals(OCamlTypes.AND.getSymbol()))) {
            res = OCamlPsiUtils.skipMeaninglessPreviousSibling(core);
            isPrev = true;
        }
        if (res != null && res.getText().equals(OCamlTypes.AND.getSymbol())) {
            values = exploreAnd(isPrev);
        }

        range = values.first;
        text = values.second;
    }

    private @NotNull Pair<TextRange, String> exploreAnd(boolean isPrev) {
        AtomicReference<String> text = new AtomicReference<>("");
        ComputeMethod<String, PsiElement> appendText;
        if (isPrev) appendText = e -> text.getAndUpdate(s -> e.getText() + s);
        else appendText = e -> text.getAndUpdate(s -> s + e.getText());

        ComputeMethod<PsiElement, PsiElement> explore;
        if (isPrev) {
            explore = e -> {
                if (e == null) return null;
                PsiElement prevSibling = e.getPrevSibling();
                if (prevSibling == null) prevSibling = e.getParent();
                return prevSibling;
            };
        } else {
            explore = e -> {
                if (e == null) return null;
                PsiElement nextSibling = e.getNextSibling();
                if (nextSibling == null) nextSibling = e.getParent();
                return nextSibling;
            };
        }

        appendText.call(core);
        PsiElement psiElement = exploreAndAppendText(explore, appendText, core, true, core);
        text.set(text.get().trim()); // remove leading/trailing whitespaces

        TextRange range;
        if (isPrev)
            range = new TextRange(psiElement.getTextRange().getStartOffset(), core.getTextRange().getEndOffset());
        else
            range = new TextRange(core.getTextRange().getStartOffset(), psiElement.getTextRange().getEndOffset());

        return new Pair<>(range, text.get());
    }

    private @NotNull PsiElement exploreAndAppendText(@NotNull ComputeMethod<PsiElement, PsiElement> explore,
                                                     ComputeMethod<String, PsiElement> appendText,
                                                     PsiElement start, boolean waitForAnd,
                                                     PsiElement lastMeaningfulStart) {
        PsiElement found = explore.call(start);
        if (found == null) return lastMeaningfulStart;
        if (found instanceof PsiWhiteSpace) appendText.call(found);
        else if (!(found instanceof PsiComment)) {
            if (waitForAnd) {
                if (!found.getText().equals(OCamlTypes.AND.getSymbol()))
                    return lastMeaningfulStart; // done
                waitForAnd = false; // found
            } else {
                if (!(found instanceof PsiStructuredElement))
                    return lastMeaningfulStart; // done
                waitForAnd = true;
            }
            appendText.call(found);
            lastMeaningfulStart = found;
        }
        return exploreAndAppendText(explore, appendText, found, waitForAnd, lastMeaningfulStart);
    }

    @Override public TextRange getTextRange() {
        return range;
    }

    @Override public @NotNull String getText() {
        return text;
    }

    @Override public PsiElement getParent() {
        return core.getParent();
    }

    public @NotNull PsiElement getCore() {
        return new PsiWrapLetWithAnd(this);
    }

    private static class PsiWrapLetWithAnd implements PsiElement {
        private final PsiElement core;
        private final PsiLetWithAnd letWithAnd;

        // fix of a bug: PsiLetWithAnd is not a valid PsiElement
        //  we need to unwrap the core.
        public PsiWrapLetWithAnd(@NotNull PsiLetWithAnd letWithAnd) {
            this.letWithAnd = letWithAnd;
            // Once upon a time, a dummy called getCore => StackOverflow :D
            this.core = letWithAnd.core;
        }

        // changed

        @Override public TextRange getTextRange() {
            return letWithAnd.getTextRange();
        }

        // unchanged

        @Override public @NotNull Project getProject() throws PsiInvalidElementAccessException {
            return core.getProject();
        }

        @Override public @NotNull Language getLanguage() {
            return core.getLanguage();
        }

        @Override public PsiManager getManager() {
            return core.getManager();
        }

        @Override public PsiElement @NotNull [] getChildren() {
            return core.getChildren();
        }

        @Override public PsiElement getParent() {
            return core.getParent();
        }

        @Override public PsiElement getFirstChild() {
            return core.getFirstChild();
        }

        @Override public PsiElement getLastChild() {
            return core.getLastChild();
        }

        @Override public PsiElement getNextSibling() {
            return core.getNextSibling();
        }

        @Override public PsiElement getPrevSibling() {
            return core.getPrevSibling();
        }

        @Override public PsiFile getContainingFile() throws PsiInvalidElementAccessException {
            return core.getContainingFile();
        }

        @Override public int getStartOffsetInParent() {
            return core.getStartOffsetInParent();
        }

        @Override public int getTextLength() {
            return core.getTextLength();
        }

        @Override public @Nullable PsiElement findElementAt(int offset) {
            return core.findElementAt(offset);
        }

        @Override public @Nullable PsiReference findReferenceAt(int offset) {
            return core.findReferenceAt(offset);
        }

        @Override public int getTextOffset() {
            return core.getTextOffset();
        }

        @SuppressWarnings("UnstableApiUsage") @Override public @NlsSafe String getText() {
            return core.getText();
        }

        @Override public char @NotNull [] textToCharArray() {
            return core.textToCharArray();
        }

        @Override public PsiElement getNavigationElement() {
            return core.getNavigationElement();
        }

        @Override public PsiElement getOriginalElement() {
            return core.getOriginalElement();
        }

        @Override public boolean textMatches(@NotNull @NonNls CharSequence text) {
            return core.textMatches(text);
        }

        @Override public boolean textMatches(@NotNull PsiElement element) {
            return core.textMatches(element);
        }

        @Override public boolean textContains(char c) {
            return core.textContains(c);
        }

        @Override public void accept(@NotNull PsiElementVisitor visitor) {
            core.accept(visitor);
        }

        @Override public void acceptChildren(@NotNull PsiElementVisitor visitor) {
            core.acceptChildren(visitor);
        }

        @Override public PsiElement copy() {
            return core.copy();
        }

        @Override public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException {
            return core.add(element);
        }

        @Override
        public PsiElement addBefore(@NotNull PsiElement element, @Nullable PsiElement anchor) throws IncorrectOperationException {
            return core.addBefore(element, anchor);
        }

        @Override
        public PsiElement addAfter(@NotNull PsiElement element, @Nullable PsiElement anchor) throws IncorrectOperationException {
            return core.addAfter(element, anchor);
        }

        @SuppressWarnings({"deprecation", "RedundantSuppression"})
        @Override public void checkAdd(@NotNull PsiElement element) throws IncorrectOperationException {
            core.checkAdd(element);
        }

        @Override public PsiElement addRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
            return core.addRange(first, last);
        }

        @Override
        public PsiElement addRangeBefore(@NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor) throws IncorrectOperationException {
            return core.addRange(first, last);
        }

        @Override
        public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor) throws IncorrectOperationException {
            return core.addRangeAfter(first, last, anchor);
        }

        @Override public void delete() throws IncorrectOperationException {
            core.delete();
        }

        @SuppressWarnings({"deprecation", "RedundantSuppression"})
        @Override public void checkDelete() throws IncorrectOperationException {
            core.checkDelete();
        }

        @Override public void deleteChildRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
            core.deleteChildRange(first, last);
        }

        @Override public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
            return core.replace(newElement);
        }

        @Override public boolean isValid() {
            return core.isValid();
        }

        @Override public boolean isWritable() {
            return core.isWritable();
        }

        @Override public @Nullable PsiReference getReference() {
            return core.getReference();
        }

        @Override public PsiReference @NotNull [] getReferences() {
            return core.getReferences();
        }

        @Override public <T> @Nullable T getCopyableUserData(@NotNull Key<T> key) {
            return core.getCopyableUserData(key);
        }

        @Override public <T> void putCopyableUserData(@NotNull Key<T> key, @Nullable T value) {
            core.putCopyableUserData(key, value);
        }

        @Override
        public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
            return core.processDeclarations(processor, state, lastParent, place);
        }

        @Override public @Nullable PsiElement getContext() {
            return core.getContext();
        }

        @Override public boolean isPhysical() {
            return core.isPhysical();
        }

        @Override public @NotNull GlobalSearchScope getResolveScope() {
            return core.getResolveScope();
        }

        @Override public @NotNull SearchScope getUseScope() {
            return core.getUseScope();
        }

        @Override public ASTNode getNode() {
            return core.getNode();
        }

        @Override public boolean isEquivalentTo(PsiElement another) {
            return core.isEquivalentTo(another);
        }

        @Override public Icon getIcon(int flags) {
            return core.getIcon(flags);
        }

        @Override public <T> @Nullable T getUserData(@NotNull Key<T> key) {
            return core.getUserData(key);
        }

        @Override public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {
            core.putUserData(key, value);
        }
    }
}
