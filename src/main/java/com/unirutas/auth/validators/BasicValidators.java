package com.unirutas.auth.validators;

import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicValidators {
    private static final Logger logger = LoggerFactory.getLogger(BasicValidators.class);

    public static boolean validateIsBasic(User user){
        return user.getSecurityPhrase() == null && user.getPhone() == null;
    }

    public static boolean basicValidation(User user, String username, String password){
        return user.getUsername().equals(username) && user.getPassword().equals(password);
    }

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
