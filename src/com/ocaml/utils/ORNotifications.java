package com.ocaml.utils;

import com.intellij.notification.*;
import com.intellij.openapi.ui.*;
import org.jetbrains.annotations.*;

import javax.swing.*;

public final class ORNotifications {

    public static void notify(OCamlNotificationData notification) {
        doNotify(notification, false);
    }

    public static Notification notifyBuild(OCamlNotificationData notification) {
        return doNotify(notification, true);
    }

    private static Notification doNotify(OCamlNotificationData notification, boolean build) {
        NotificationGroupManager instance = NotificationGroupManager.getInstance();
        NotificationGroup notificationGroup = instance.getNotificationGroup(build ? "OCaml Build Log" : "OCaml Plugin");
        Notification realNotification;
        // fill
        if (notification.myMessageType != null) {
            realNotification = notificationGroup.createNotification(notification.myContent, notification.myMessageType);
        } else {
            if (notification.myTitle == null) notification.myTitle = notification.myContent;
            realNotification = notificationGroup.createNotification(notification.myTitle, notification.myNotificationType);
        }
        if (notification.myTitle != null) realNotification.setContent(notification.myContent);
        if (notification.mySubtitle != null) realNotification.setSubtitle(notification.mySubtitle);
        if (notification.myIcon != null) realNotification.setIcon(notification.myIcon);
        if (notification.myListener != null) realNotification.setListener(notification.myListener);
        // fire
        realNotification.notify(null);
        return realNotification;
    }

    public static class OCamlNotificationData {
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

}
