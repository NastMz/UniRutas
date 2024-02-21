package com.unirutas.auth.handlers.implementation;

import com.unirutas.auth.handlers.interfaces.IAuthenticationHandler;
import com.unirutas.models.User;

/**
 * Default implementation of the AuthenticationHandler interface.
 * This class configures and manages a chain of responsibility for user authentication.
 */
public class DefaultAuthenticationHandler implements IAuthenticationHandler {
    private IAuthenticationHandler successor;

    /**
     * Constructor for the DefaultAuthenticationHandler class.
     * Configures the chain of responsibility with different authentication handlers.
     */
    public DefaultAuthenticationHandler() {
        // Configure the chain of responsibility
        BasicAuthenticationHandler basicAuth = new BasicAuthenticationHandler();
        SecurityPhraseAuthenticationHandler phraseAuth = new SecurityPhraseAuthenticationHandler();
        PhoneAuthenticationHandler phoneAuth = new PhoneAuthenticationHandler();
        MultiFactorAuthenticationHandler multiFactorAuth = new MultiFactorAuthenticationHandler();

        basicAuth.setSuccessor(phraseAuth);
        phraseAuth.setSuccessor(phoneAuth);
        phoneAuth.setSuccessor(multiFactorAuth);

        // Set the first link in the chain
        successor = basicAuth;
    }

    @Override
    public boolean authenticate(User user, String username, String password, String phone, String securityPhrase) {
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
