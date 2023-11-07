package com.unirutas.controllers;

import com.unirutas.chain.of.responsibility.AuthenticationHandler;
import com.unirutas.chain.of.responsibility.BasicAuthenticationHandler;
import com.unirutas.chain.of.responsibility.SecurityPhraseAuthenticationHandler;
import com.unirutas.models.User;

public class AuthenticationController implements AuthenticationHandler {
    private AuthenticationHandler successor;

    @Override
    public boolean authenticate(User user) {
        BasicAuthenticationHandler basic = new BasicAuthenticationHandler();
        setSuccessor(basic);

        SecurityPhraseAuthenticationHandler secure = new SecurityPhraseAuthenticationHandler();
        basic.setSuccessor(secure);

        return successor.authenticate(user);
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
