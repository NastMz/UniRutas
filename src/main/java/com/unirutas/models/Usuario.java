package com.unirutas.models;

import java.util.ArrayList;
import java.util.List;

public abstract class Usuario extends Persona {
    private final String nombreUsuario;
    private String contrasena;
    private final List<Alerta> alertasNoLeidas;

    public Usuario(String nombre, String codigo, String nombreUsuario, String contrasena) {
        super(nombre, codigo);
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.alertasNoLeidas = new ArrayList<>();
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public boolean cambiarContrasena(String nuevaContrasena) {
        this.contrasena = nuevaContrasena;
        return true;
    }

    public void recibirAlerta(Alerta alerta) {
        alertasNoLeidas.add(alerta); // Agrega la alerta a la lista de alertas no leídas
    }

    public List<Alerta> obtenerAlertasNoLeidas() {
        return alertasNoLeidas;
    }

    public void marcarAlertaLeida(Alerta alerta) {
        alertasNoLeidas.remove(alerta);
    }

}


