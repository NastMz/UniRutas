package com.unirutas.auth.validators;

import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * PhoneValidators provide validation methods for phone-based user authentication.
 */
public class PhoneValidators {
    private static final Logger logger = LoggerFactory.getLogger(PhoneValidators.class);

    /**
     * Checks if the user has a phone and no security phrase.
     *
     * @param user The user to check.
     * @return True if the user has a phone and no security phrase, false otherwise.
     */
    public static boolean validateIsPhone(User user) {
        return user.getPhone() != null && user.getSecurityPhrase() == null;
    }

    /**
     * Validates phone-based credentials.
     *
     * @param user  The user to authenticate.
     * @param phone The phone number for authentication.
     * @return True if authentication is successful, false otherwise.
     */
    public static boolean phoneValidation(User user, String phone) {
        return user.getPhone().equals(phone);
    }

    /**
     * Validates user credentials and phone, logging authentication results.
     *
     * @param user     The user to authenticate.
     * @param username The username for authentication.
     * @param password The password for authentication.
     * @param phone    The phone number for authentication.
     * @return True if authentication is successful, false otherwise.
     */
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
