package com.unirutas.auth.validators;

import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityPhraseValidators {
    private static final Logger logger = LoggerFactory.getLogger(SecurityPhraseValidators.class);

    public static boolean validateIsSecurityPhrase(User user) {
        return user.getSecurityPhrase() != null && user.getPhone() == null;
    }

    public static boolean securityPhraseValidation(User user, String securityPhrase) {
        return user.getSecurityPhrase().equals(securityPhrase);
    }

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
