package com.unirutas.auth.implementation;

import com.unirutas.auth.handlers.AuthenticationHandler;
import com.unirutas.models.User;

import javax.swing.*;

public class SecurityPhraseAuthenticationHandler implements AuthenticationHandler {
    private AuthenticationHandler successor;

    @Override
    public boolean authenticate(User user) {
        if (user.getSecurityPhrase() != null){
            String username = JOptionPane.showInputDialog(null, "Ingrese su nombre de usuario: ");
            String password = JOptionPane.showInputDialog(null, "Ingrese su contraseña: ");
            String securityPhrase = JOptionPane.showInputDialog(null, "Ingrese su frase de seguridad: ");

            if (user.getUsername().equals(username) && user.getPassword().equals(password) && user.getSecurityPhrase().equals(securityPhrase)){
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
