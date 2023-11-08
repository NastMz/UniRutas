package com.unirutas.auth.implementation;

import com.unirutas.auth.handlers.AuthenticationHandler;
import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The SecurityPhraseAuthenticationHandler is responsible for user authentication using a security phrase.
 * It checks username, password, and security phrase for authentication.
 */
public class SecurityPhraseAuthenticationHandler implements AuthenticationHandler {
    private AuthenticationHandler successor;
    private static final Logger logger = LoggerFactory.getLogger(SecurityPhraseAuthenticationHandler.class);

    /**
     * Authenticates the user based on the provided credentials and additional factors.
     *
     * @param user           The user to authenticate.
     * @param username       The username for authentication.
     * @param password       The password for authentication.
     * @param phone          The phone number for phone or multi-factor authentication. (In this case is null)
     * @param securityPhrase The security phrase for security phrase or multi-factor authentication.
     * @return True if authentication is successful, false otherwise.
     */
    @Override
    public boolean authenticate(User user, String username, String password, String phone, String securityPhrase) {
        if (user.getSecurityPhrase() != null && user.getPhone() == null){
            logger.info("Validating credentials and security phrase...");
            if (user.getUsername().equals(username) && user.getPassword().equals(password) && user.getSecurityPhrase().equals(securityPhrase)){
                String message = "Successful authentication for "+ user.getName()+".";
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
