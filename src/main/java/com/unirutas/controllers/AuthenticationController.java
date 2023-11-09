package com.unirutas.controllers;

import com.unirutas.auth.handlers.interfaces.IAuthenticationHandler;
import com.unirutas.auth.handlers.implementation.BasicAuthenticationHandler;
import com.unirutas.auth.handlers.implementation.MultiFactorAuthenticationHandler;
import com.unirutas.auth.handlers.implementation.PhoneAuthenticationHandler;
import com.unirutas.auth.handlers.implementation.SecurityPhraseAuthenticationHandler;
import com.unirutas.models.User;


/**
 * The AuthenticationController orchestrates the authentication process using a chain of responsibility.
 * It configures authentication handlers and delegates authentication to them.
 */
public class AuthenticationController implements IAuthenticationHandler {
    private IAuthenticationHandler successor;

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
    public IAuthenticationHandler getSuccessor() {
        return successor;
    }

    @Override
    public void setSuccessor(IAuthenticationHandler successor) {
        this.successor = successor;
    }
}
