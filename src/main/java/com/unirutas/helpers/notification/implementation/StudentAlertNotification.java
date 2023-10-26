package com.unirutas.helpers.notification.implementation;

import com.unirutas.core.builder.query.interfaces.ICustomQueryBuilder;
import com.unirutas.core.builder.query.types.Tuple;
import com.unirutas.core.providers.CustomQueryBuilderProvider;
import com.unirutas.helpers.notification.template.AlertNotification;
import com.unirutas.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StudentAlertNotification extends AlertNotification {
    private final ICustomQueryBuilder queryBuilder = CustomQueryBuilderProvider.getFactory().createCustomQueryBuilder(StudentSubscription.class);
    private final Logger logger = LoggerFactory.getLogger(StudentAlertNotification.class);

    private boolean shouldNotify(User user, Alert alert) {
        List<List<Tuple<String, Object>>> results = queryBuilder.select()
                .where("student_code", user.getCode())
                .and("service_id", alert.getService())
                .execute();

        return !results.isEmpty();
    }

    @Override
    protected void sendNotification(User user, Alert alert) {
        // TODO: Implement notification sending logic.
        if (shouldNotify(user, alert)) {
            logger.info("Sending notification to student " + user.getName() + ": " + alert.getDescription());
        }
    }
}