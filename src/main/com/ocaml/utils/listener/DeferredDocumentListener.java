package com.ocaml.utils.listener;

import org.jetbrains.annotations.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.event.*;

/**
 * Register a deferred document listener to the JTextComponent (ex: a JTextField).
 * The listener is relying on a timer that will trigger the action listener once the
 * delay was consumed. The delay is reset every time the user is pressing a key.
 * This is allowing us to call the action Listener, once the user "stopped" typing for a bit,
 * reducing the number of calls to the action listener.
 */
public final class DeferredDocumentListener implements DocumentListener {

    /** @see DeferredDocumentListener **/
    public static void addDeferredDocumentListener(@NotNull JTextComponent textComponent,
                                                   @NotNull ActionListener actionListener,
                                                   @Nullable Runnable onStart,
                                                   int delay) {
        Document document = textComponent.getDocument();
        DeferredDocumentListener deferredDocumentListener = new DeferredDocumentListener(delay, onStart, actionListener);
        document.addDocumentListener(deferredDocumentListener);
        textComponent.addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e) {}
            @Override public void focusLost(FocusEvent e) {
                deferredDocumentListener.stopTimer();
            }
        });
    }

    private final Runnable myOnStart;
    private final Timer myTimer;
    private boolean myHasStarted;

    /**
     *
     * @param delay run the action listener when the delay was consumed
     * @param onStart run this runnable when the timer is about to start. It may be if there
     *                is a need to invalidate "others" fields, as we are waiting for the user to
     *                stop its input to trigger actionListener and fill again the "others" fields.
     * @param actionListener the action listener will be called when the timer was consumed
     */
    private DeferredDocumentListener(int delay, @Nullable Runnable onStart, ActionListener actionListener) {
        myOnStart = onStart == null ? () -> {} : onStart;
        myTimer = new Timer(delay, e -> {
            stopTimer(); // stop until restarted
            actionListener.actionPerformed(e);
        });
        myTimer.setRepeats(true);
    }

    private void start() {
        // notify start
        if (!myHasStarted) {
            myOnStart.run();
            myHasStarted = true;
        }
        // start
        myTimer.restart();
    }

    private void stopTimer() { myTimer.stop(); myHasStarted = false; }

    // reset on every event
    @Override public void insertUpdate(DocumentEvent e) { start(); }
    @Override public void removeUpdate(DocumentEvent e) { start(); }
    @Override public void changedUpdate(DocumentEvent e) { start(); }
}
