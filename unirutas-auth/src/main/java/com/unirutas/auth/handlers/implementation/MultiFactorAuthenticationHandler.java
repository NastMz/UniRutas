package com.unirutas.auth.handlers.implementation;

import com.unirutas.auth.handlers.interfaces.IAuthenticationHandler;
import com.unirutas.auth.validators.MultiFactorValidators;
import com.unirutas.models.User;


/**
 * The MultiFactorAuthenticationHandler is responsible for user authentication using a security phrase and phone.
 * It checks username, password, security phrase, and phone for authentication.
 */
public class MultiFactorAuthenticationHandler implements IAuthenticationHandler {
    private IAuthenticationHandler successor;

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
        if (MultiFactorValidators.validateIsMultiFactor(user)){
           return MultiFactorValidators.validateCredentialsAndMultiFactor(user, username, password, phone, securityPhrase);
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
