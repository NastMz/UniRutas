package com.unirutas.auth.validators;

import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SecurityPhraseValidators provide validation methods for security phrase-based user authentication.
 */
public class SecurityPhraseValidators {
    private static final Logger logger = LoggerFactory.getLogger(SecurityPhraseValidators.class);

    /**
     * Checks if the user has a security phrase and no phone.
     *
     * @param user The user to check.
     * @return True if the user has a security phrase and no phone, false otherwise.
     */
    public static boolean validateIsSecurityPhrase(User user) {
        return user.getSecurityPhrase() != null && user.getPhone() == null;
    }

    /**
     * Validates security phrase-based credentials.
     *
     * @param user           The user to authenticate.
     * @param securityPhrase The security phrase for authentication.
     * @return True if authentication is successful, false otherwise.
     */
    public static boolean securityPhraseValidation(User user, String securityPhrase) {
        return user.getSecurityPhrase().equals(securityPhrase);
    }

    /**
     * Validates user credentials and security phrase, logging authentication results.
     *
     * @param user           The user to authenticate.
     * @param username       The username for authentication.
     * @param password       The password for authentication.
     * @param securityPhrase The security phrase for authentication.
     * @return True if authentication is successful, false otherwise.
     */
    public static boolean validateCredentialsAndSecurityPhrase(User user, String username, String password, String securityPhrase) {
        logger.info("Validating credentials and security phrase...");
        if (BasicValidators.basicValidation(user, username, password) && securityPhraseValidation(user, securityPhrase)){
            logger.info("Successful authentication for "+ user.getName()+".");
            return true;
        } else {
            logger.error("Authentication failed...");
            return false;
        }
    }
}
