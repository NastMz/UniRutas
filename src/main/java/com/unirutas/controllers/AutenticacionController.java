package com.unirutas.controllers;

import com.unirutas.models.Usuario;

public class AutenticacionController {
    public boolean autenticarUsuario(Usuario usuario, String nombreUsuario, String contrasena) {
        // Verificar si las credenciales coinciden con el usuario
        // Credenciales inválidas, autenticación fallida
        return usuario != null && usuario.getNombreUsuario().equals(nombreUsuario) && usuario.getContrasena().equals(contrasena); // Credenciales válidas, autenticación exitosa
    }
}
