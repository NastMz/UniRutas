package com.unirutas.chain.of.responsibility;

import com.unirutas.models.User;

public interface AuthenticationHandler {
    public boolean authenticate(User user);
    public AuthenticationHandler getSuccessor();
    public void setSuccessor(AuthenticationHandler successor);
}
