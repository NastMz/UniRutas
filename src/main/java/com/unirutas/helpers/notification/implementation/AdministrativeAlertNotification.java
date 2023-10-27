package com.unirutas.helpers.notification.implementation;

import com.unirutas.helpers.notification.template.AlertNotification;
import com.unirutas.models.Alert;
import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdministrativeAlertNotification extends AlertNotification {
    private final Logger logger = LoggerFactory.getLogger(AdministrativeAlertNotification.class);

    @Override
    protected void sendNotification(User user, Alert alert) {
        // TODO: Implement notification sending logic.
        logger.info("Sending notification to administrative " + user.getName() + ": " + alert.getDescription());
    }
}
