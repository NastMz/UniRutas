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
     * Authenticates the user based on the provided credentials and additional factors.
     *
     * @param user           The user to authenticate.
     * @param username       The username for authentication.
     * @param password       The password for authentication.
     * @param phone          The phone number for phone or multi-factor authentication. (In this case is null)
     * @param securityPhrase The security phrase for security phrase or multi-factor authentication. (In this case is null)
     * @return True if authentication is successful, false otherwise.
     */
    @Override
    public boolean authenticate(User user, String username, String password, String phone, String securityPhrase) {
        if (user.getSecurityPhrase() == null && user.getPhone() == null){
            logger.info("Validating credentials...");
            if (user.getUsername().equals(username) && user.getPassword().equals(password)){
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
