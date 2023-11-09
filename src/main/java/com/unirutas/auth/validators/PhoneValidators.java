package com.unirutas.auth.validators;

import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhoneValidators {
    private static final Logger logger = LoggerFactory.getLogger(PhoneValidators.class);

    public static boolean validateIsPhone(User user) {
        return user.getPhone() != null && user.getSecurityPhrase() == null;
    }

    public static boolean phoneValidation(User user, String phone) {
        return user.getPhone().equals(phone);
    }

    public static boolean validateCredentialsAndPhone(User user, String username, String password, String phone) {
        logger.info("Validating credentials and phone...");
        if (BasicValidators.basicValidation(user, username, password) && phoneValidation(user, phone)){
            logger.info("Successful authentication for "+ user.getName()+".");
            return true;
        } else {
            logger.error("Authentication failed...");
            return false;
        }
    }
}
