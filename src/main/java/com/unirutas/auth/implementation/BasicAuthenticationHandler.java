package com.unirutas.auth.implementation;

import com.unirutas.auth.handlers.AuthenticationHandler;
import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The BasicAuthenticationHandler is responsible for basic user authentication.
 * It checks username and password for authentication.
 */
public class BasicAuthenticationHandler implements AuthenticationHandler {
    private AuthenticationHandler successor;
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationHandler.class);

    /**
     * Authenticate the user with the provided username and password.
     *
     * @param user           The user to authenticate.
     * @param username       The username for authentication.
     * @param password       The password for authentication.
     * @param securityPhrase The security phrase for authentication (In this case is null).
     * @return True if authentication is successful, false otherwise.
     */
    @Override
    public boolean authenticate(User user, String username, String password, String securityPhrase) {
        if (user.getSecurityPhrase() == null){
            if (user.getUsername().equals(username) && user.getPassword().equals(password)){
                String message = "Successful authentication for "+ user.getName()+".";
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
