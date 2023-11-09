package com.unirutas.controllers;

import com.unirutas.auth.handlers.interfaces.IAuthenticationHandler;
import com.unirutas.auth.handlers.implementation.DefaultAuthenticationHandler;
import com.unirutas.models.User;


/**
 * Controller responsible for user authentication using the chain of responsibility.
 */
public class AuthenticationController {
    private IAuthenticationHandler authenticationHandler;

    /**
     * Constructor for the AuthenticationController class.
     * Initializes the controller with a default implementation of AuthenticationHandler.
     */
    public AuthenticationController() {
        authenticationHandler = new DefaultAuthenticationHandler();
    }

    /**
     * Method to authenticate the user using the chain of responsibility.
     *
     * @param user           The User object representing the user.
     * @param username       The username provided for authentication.
     * @param password       The password provided for authentication.
     * @param phone          The phone number provided for authentication.
     * @param securityPhrase The security phrase provided for authentication.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticate(User user, String username, String password, String phone, String securityPhrase) {
        return authenticationHandler.authenticate(user, username, password, phone, securityPhrase);
    }
}
