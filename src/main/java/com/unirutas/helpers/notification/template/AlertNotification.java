package com.unirutas.helpers.notification.template;

import com.unirutas.models.Alert;
import com.unirutas.models.User;

import java.util.List;

public abstract class AlertNotification {

    public void notifyUsers(List<User> users, Alert alert) {
        for (User user : users) {
            sendNotification(user, alert);
        }
    }

    protected abstract void sendNotification(User user, Alert alert);
}
