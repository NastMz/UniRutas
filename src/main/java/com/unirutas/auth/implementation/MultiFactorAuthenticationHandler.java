package com.unirutas.auth.implementation;

import com.unirutas.auth.handlers.AuthenticationHandler;
import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MultiFactorAuthenticationHandler implements AuthenticationHandler {
    private AuthenticationHandler successor;
    private static final Logger logger = LoggerFactory.getLogger(MultiFactorAuthenticationHandler.class);

    @Override
    public boolean authenticate(User user, String username, String password, String phone, String securityPhrase) {
        if (user.getPhone() != null && user.getSecurityPhrase() != null){
            logger.info("Validating credentials, phone and security phrase...");
            if (user.getUsername().equals(username) && user.getPassword().equals(password) && user.getPhone().equals(phone) && user.getSecurityPhrase().equals(securityPhrase)){
                logger.info("Successful authentication for "+ user.getName()+".");
                return true;
            } else {
                logger.error("Authentication failed...");
                return false;
            }
        } else {
            return successor.authenticate(user, username, password, phone, securityPhrase);
        }
    }

    @Override
    public AuthenticationHandler getSuccessor() {
        return successor;
    }

    @Override
    public void setSuccessor(AuthenticationHandler successor) {
        this.successor = successor;
    }
}
