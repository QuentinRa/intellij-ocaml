package com.ocaml.lang.core;

import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.ocaml.lang.utils.OCamlPsiUtils;
import com.ocaml.utils.ComputeMethod;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiStructuredElement;
import com.or.lang.core.psi.impl.PsiLetImpl;
import com.or.lang.core.type.ORTokenElementType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

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
        if (isPrev) appendText = e -> text.getAndUpdate(s -> e.getText()+s);
        else appendText = e -> text.getAndUpdate(s -> s+e.getText());

        ComputeMethod<PsiElement, PsiElement> explore;
        if (isPrev) {
            explore = e -> {
                if (e == null) return null;
                PsiElement prevSibling = e.getPrevSibling();
                if (prevSibling == null) prevSibling = e.getParent();
                return prevSibling;
            };
        }
        else {
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
}
