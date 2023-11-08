package com.unirutas.controllers;

import com.unirutas.auth.handlers.AuthenticationHandler;
import com.unirutas.auth.implementation.BasicAuthenticationHandler;
import com.unirutas.auth.implementation.MultiFactorAuthenticationHandler;
import com.unirutas.auth.implementation.PhoneAuthenticationHandler;
import com.unirutas.auth.implementation.SecurityPhraseAuthenticationHandler;
import com.unirutas.models.User;


/**
 * The AuthenticationController orchestrates the authentication process using a chain of responsibility.
 * It configures authentication handlers and delegates authentication to them.
 */
public class AuthenticationController implements AuthenticationHandler {
    private AuthenticationHandler successor;

    /**
     * Authenticate the user using the configured authentication handlers.
     *
     * @param user           The user to authenticate.
     * @param username       The username for authentication.
     * @param password       The password for authentication.
     * @param securityPhrase The security phrase for authentication.
     * @return True if authentication is successful, false otherwise.
     */
    @Override
    public boolean authenticate(User user, String username, String password, String phone, String securityPhrase) {
        BasicAuthenticationHandler basicAuth = new BasicAuthenticationHandler();
        setSuccessor(basicAuth);

        SecurityPhraseAuthenticationHandler phraseAuth = new SecurityPhraseAuthenticationHandler();
        basicAuth.setSuccessor(phraseAuth);

        PhoneAuthenticationHandler phoneAuth = new PhoneAuthenticationHandler();
        phraseAuth.setSuccessor(phoneAuth);

        MultiFactorAuthenticationHandler multiFactorAuth = new MultiFactorAuthenticationHandler();
        phoneAuth.setSuccessor(multiFactorAuth);

        return successor.authenticate(user, username, password, phone, securityPhrase);
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
