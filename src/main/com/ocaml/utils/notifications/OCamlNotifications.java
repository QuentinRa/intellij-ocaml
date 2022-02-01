package com.ocaml.utils.notifications;

import com.intellij.notification.*;
import org.jetbrains.annotations.NotNull;

/**
 * Utility to show notifications
 */
public final class OCamlNotifications {

    public static void notify(OCamlNotificationData notification) {
        doNotify(notification, false);
    }

    public static @NotNull Notification notifyBuild(OCamlNotificationData notification) {
        return doNotify(notification, true);
    }

    private static @NotNull Notification doNotify(@NotNull OCamlNotificationData notification, boolean build) {
        NotificationGroupManager instance = NotificationGroupManager.getInstance();
        NotificationGroup notificationGroup = instance.getNotificationGroup(
                build ? OCamlNotificationsBus.BUILD : OCamlNotificationsBus.NORMAL
        );
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

}
