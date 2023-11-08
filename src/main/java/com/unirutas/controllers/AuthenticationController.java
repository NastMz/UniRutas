package com.unirutas.controllers;

import com.unirutas.auth.handlers.AuthenticationHandler;
import com.unirutas.auth.implementation.BasicAuthenticationHandler;
import com.unirutas.auth.implementation.SecurityPhraseAuthenticationHandler;
import com.unirutas.models.User;

public class AuthenticationController implements AuthenticationHandler {
    private AuthenticationHandler successor;

    @Override
    public boolean authenticate(User user, String username, String password, String securityPhrase) {
        BasicAuthenticationHandler basic = new BasicAuthenticationHandler();
        setSuccessor(basic);

        SecurityPhraseAuthenticationHandler secure = new SecurityPhraseAuthenticationHandler();
        basic.setSuccessor(secure);

        return successor.authenticate(user, username, password, securityPhrase);
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
