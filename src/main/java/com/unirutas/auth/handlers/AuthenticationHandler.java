package com.unirutas.auth.handlers;

import com.unirutas.models.User;

/**
 * The AuthenticationHandler interface defines the contract for handling user authentication.
 * Implementing classes should provide authentication logic and support chaining to the next handler.
 */
public interface AuthenticationHandler {
    /**
     * Authenticate the user with the given credentials.
     *
     * @param user           The user to authenticate.
     * @param username       The username for authentication.
     * @param password       The password for authentication.
     * @param securityPhrase The security phrase for authentication.
     * @return True if authentication is successful, false otherwise.
     */
    public boolean authenticate(User user, String username, String password, String phone, String securityPhrase);

    /**
     * Get the next authentication handler in the chain.
     *
     * @return The next authentication handler or null if this is the last handler.
     */
    public AuthenticationHandler getSuccessor();

    /**
     * Set the next authentication handler in the chain.
     *
     * @param successor The next authentication handler to set.
     */
    public void setSuccessor(AuthenticationHandler successor);
}