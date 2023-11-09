package com.unirutas.auth.handlers.implementation;

import com.unirutas.auth.handlers.interfaces.IAuthenticationHandler;
import com.unirutas.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The MultiFactorAuthenticationHandler is responsible for user authentication using a security phrase and phone.
 * It checks username, password, security phrase, and phone for authentication.
 */
public class MultiFactorAuthenticationHandler implements IAuthenticationHandler {
    private IAuthenticationHandler successor;
    private static final Logger logger = LoggerFactory.getLogger(MultiFactorAuthenticationHandler.class);

    /**
     * Authenticates the user based on the provided credentials and additional factors.
     *
     * @param user           The user to authenticate.
     * @param username       The username for authentication.
     * @param password       The password for authentication.
     * @param phone          The phone number for phone or multi-factor authentication.
     * @param securityPhrase The security phrase for security phrase or multi-factor authentication.
     * @return True if authentication is successful, false otherwise.
     */
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
    public IAuthenticationHandler getSuccessor() {
        return successor;
    }

    @Override
    public void setSuccessor(IAuthenticationHandler successor) {
        this.successor = successor;
    }
}
