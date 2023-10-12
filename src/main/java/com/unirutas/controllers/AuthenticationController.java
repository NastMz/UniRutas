package com.unirutas.controllers;

import com.unirutas.models.User;

public class AuthenticationController {
    public boolean authenticateUser(User user, String username, String password) {
        // Verificar si las credenciales coinciden con el user
        // Credenciales inválidas, autenticación fallida
        return user != null && user.getUsername().equals(username) && user.getPassword().equals(password); // Credenciales válidas, autenticación exitosa
    }
}
