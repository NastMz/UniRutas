package com.unirutas.helpers.notification.implementation;

import com.unirutas.core.builder.query.interfaces.ICustomQueryBuilder;
import com.unirutas.core.builder.query.types.Tuple;
import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.core.providers.CustomQueryBuilderProvider;
import com.unirutas.helpers.notification.template.AlertNotification;
import com.unirutas.models.*;
import com.unirutas.services.implementation.StudentSubscriptionServices;
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