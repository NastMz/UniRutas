package com.unirutas.auth.validators;

import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * BasicValidators provide validation methods for basic user authentication.
 */
public class BasicValidators {
    private static final Logger logger = LoggerFactory.getLogger(BasicValidators.class);

    /**
     * Checks if the user authentication is basic (no phone or security phrase).
     *
     * @param user The user to check.
     * @return True if the authentication is basic, false otherwise.
     */
    public static boolean validateIsBasic(User user){
        return user.getSecurityPhrase() == null && user.getPhone() == null;
    }

    /**
     * Validates basic credentials (username and password).
     *
     * @param user     The user to authenticate.
     * @param username The username for authentication.
     * @param password The password for authentication.
     * @return True if authentication is successful, false otherwise.
     */
    public static boolean basicValidation(User user, String username, String password){
        return user.getUsername().equals(username) && user.getPassword().equals(password);
    }

    /**
     * Validates user credentials and logs authentication results.
     *
     * @param user     The user to authenticate.
     * @param username The username for authentication.
     * @param password The password for authentication.
     * @return True if authentication is successful, false otherwise.
     */
    public static boolean validateCredentials(User user, String username, String password){
        logger.info("Validating credentials...");
        if (basicValidation(user, username, password)){
            logger.info("Successful authentication for "+ user.getName()+".");
            return true;
        } else {
            logger.error("Authentication failed...");
            return false;
        }
    }
}
