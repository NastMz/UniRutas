package com.unirutas.auth.handlers;

import com.unirutas.models.User;

public interface AuthenticationHandler {
    public boolean authenticate(User user, String username, String password, String securityPhrase);
    public AuthenticationHandler getSuccessor();
    public void setSuccessor(AuthenticationHandler successor);
}
