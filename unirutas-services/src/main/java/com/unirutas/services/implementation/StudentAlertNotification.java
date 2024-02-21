package com.unirutas.services.implementation;

import com.flexcore.dependency.annotations.Inject;
import com.unirutas.models.Alert;
import com.unirutas.models.User;
import com.unirutas.services.templates.AlertNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentAlertNotification extends AlertNotification {
    @Inject
    private StudentSubscriptionServices studentSubscriptionServices;
    private final Logger logger = LoggerFactory.getLogger(StudentAlertNotification.class);

    private boolean shouldNotify(User user, Alert alert) {
        return studentSubscriptionServices.existsById(user.getCode(), alert.getService());
    }

    @Override
    protected void sendNotification(User user, Alert alert) {
        // TODO: Implement notification sending logic.
        if (shouldNotify(user, alert)) {
            logger.info("Sending notification to student " + user.getName() + ": " + alert.getDescription());
        }
    }
}