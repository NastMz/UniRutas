package com.unirutas.auth.validators;

import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiFactorValidators {
    private static final Logger logger = LoggerFactory.getLogger(MultiFactorValidators.class);

    public static boolean validateIsMultiFactor(User user) {
        return user.getPhone() != null && user.getSecurityPhrase() != null;
    }

    public static boolean multiFactorValidation(User user, String phone, String securityPhrase) {
        return PhoneValidators.phoneValidation(user, phone) && SecurityPhraseValidators.securityPhraseValidation(user, securityPhrase);
    }

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
