package com.unirutas.auth.validators;

import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * MultiFactorValidators provide validation methods for multi-factor user authentication.
 */
public class MultiFactorValidators {
    private static final Logger logger = LoggerFactory.getLogger(MultiFactorValidators.class);

    /**
     * Checks if the user has both a phone and a security phrase.
     *
     * @param user The user to check.
     * @return True if the user has both a phone and a security phrase, false otherwise.
     */
    public static boolean validateIsMultiFactor(User user) {
        return user.getPhone() != null && user.getSecurityPhrase() != null;
    }

    /**
     * Validates multi-factor credentials.
     *
     * @param user           The user to authenticate.
     * @param phone          The phone number for authentication.
     * @param securityPhrase The security phrase for authentication.
     * @return True if authentication is successful, false otherwise.
     */
    public static boolean multiFactorValidation(User user, String phone, String securityPhrase) {
        return PhoneValidators.phoneValidation(user, phone) && SecurityPhraseValidators.securityPhraseValidation(user, securityPhrase);
    }

    /**
     * Validates user credentials, phone, and security phrase, logging authentication results.
     *
     * @param user           The user to authenticate.
     * @param username       The username for authentication.
     * @param password       The password for authentication.
     * @param phone          The phone number for authentication.
     * @param securityPhrase The security phrase for authentication.
     * @return True if authentication is successful, false otherwise.
     */
    public static boolean validateCredentialsAndMultiFactor(User user, String username, String password, String phone, String securityPhrase) {
        logger.info("Validating credentials, phone and security phrase...");
        if (BasicValidators.basicValidation(user, username, password) && multiFactorValidation(user, phone, securityPhrase)){
            logger.info("Successful authentication for "+ user.getName()+".");
            return true;
        } else {
            logger.error("Authentication failed...");
            return false;
        }
    }
}
