package com.unirutas.auth.implementation;

import com.unirutas.auth.handlers.AuthenticationHandler;
import com.unirutas.core.builder.query.implementation.sql.SQLCustomQueryBuilder;
import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BasicAuthenticationHandler implements AuthenticationHandler {
    private AuthenticationHandler successor;
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationHandler.class);

    @Override
    public boolean authenticate(User user, String username, String password, String securityPhrase) {
        if (user.getSecurityPhrase() == null){
            if (user.getUsername().equals(username) && user.getPassword().equals(password)){
                String message = "Successful authentication!!";
                logger.info(message);
                return true;
            } else {
                String message = "Authentication failed...";
                logger.error(message);
                return false;
            }
        } else {
            return successor.authenticate(user, username, password, securityPhrase);
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
