package com.ocaml.utils.notifications;

import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.ui.MessageType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class OCamlNotificationData {
    public @Nullable String myTitle;
    public @Nullable String mySubtitle;
    public @NotNull String myContent;
    public @Nullable MessageType myMessageType;
    public @NotNull NotificationType myNotificationType;
    public @Nullable Icon myIcon;
    public @Nullable NotificationListener myListener;

    public OCamlNotificationData(@NotNull String content) {
        myContent = content;
        myNotificationType = NotificationType.INFORMATION;
    }
}
