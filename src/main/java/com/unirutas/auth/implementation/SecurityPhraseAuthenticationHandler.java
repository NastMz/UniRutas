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
     * Authenticate the user with the provided username, password, and security phrase.
     *
     * @param user           The user to authenticate.
     * @param username       The username for authentication.
     * @param password       The password for authentication.
     * @param securityPhrase The security phrase for authentication.
     * @return True if authentication is successful, false otherwise.
     */
    @Override
    public boolean authenticate(User user, String username, String password, String securityPhrase) {
        if (user.getSecurityPhrase() != null){
            if (user.getUsername().equals(username) && user.getPassword().equals(password) && user.getSecurityPhrase().equals(securityPhrase)){
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
