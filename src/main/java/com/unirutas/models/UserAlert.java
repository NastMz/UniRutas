package com.unirutas.models;

import java.util.List;

public class UserAlert {

    private final List<Alert> unreadAlerts;
    private final User user;

    public UserAlert(User user, List<Alert> unreadAlerts) {
        this.user = user;
        this.unreadAlerts = unreadAlerts;
    }

    public void receiveAlert(Alert alert) {
        unreadAlerts.add(alert); // Agrega la alert a la lista de alertas no leídas
    }

    public List<Alert> getUnreadAlerts() {
        return unreadAlerts;
    }

    public void setAlertAsRead(Alert alert) {
        unreadAlerts.remove(alert);
    }
}
