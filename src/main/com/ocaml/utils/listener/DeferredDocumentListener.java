package com.ocaml.utils.listener;

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
    public static void addDeferredDocumentListener(JTextComponent textComponent,
                                                   ActionListener actionListener,
                                                   int delay) {
        Document document = textComponent.getDocument();
        DeferredDocumentListener deferredDocumentListener = new DeferredDocumentListener(delay, actionListener);
        document.addDocumentListener(deferredDocumentListener);
        textComponent.addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e) {
                deferredDocumentListener.startTimer();
            }

            @Override public void focusLost(FocusEvent e) {
                deferredDocumentListener.stopTimer();
            }
        });
    }

    private final Timer myTimer;
    private DeferredDocumentListener(int delay, ActionListener actionListener) {
        myTimer = new Timer(delay, actionListener);
        myTimer.setRepeats(true);
    }

    private void startTimer() { myTimer.start(); }
    private void stopTimer() { myTimer.stop(); }
    // reset on every event
    @Override public void insertUpdate(DocumentEvent e) { myTimer.restart(); }
    @Override public void removeUpdate(DocumentEvent e) { myTimer.restart(); }
    @Override public void changedUpdate(DocumentEvent e) { myTimer.restart(); }
}
