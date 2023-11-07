package com.unirutas.chain.of.responsibility;

import com.unirutas.models.User;

import javax.swing.*;

public class BasicAuthenticationHandler implements AuthenticationHandler{
    private AuthenticationHandler successor;

    @Override
    public boolean authenticate(User user) {
        if (user.getSecurityPhrase() == null){
            String username = JOptionPane.showInputDialog(null, "Ingrese su nombre de usuario: ");
            String password = JOptionPane.showInputDialog(null, "Ingrese su contraseña: ");

            if (user.getUsername().equals(username) && user.getPassword().equals(password)){
                System.out.println("Autenticación exitosa!!");
                return true;
            } else {
                System.out.println("Autenticación fallida...");
                return false;
            }
        } else {
            return successor.authenticate(user);
        }
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
