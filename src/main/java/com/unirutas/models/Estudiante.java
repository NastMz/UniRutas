package com.unirutas.models;

import java.util.List;

public class Estudiante extends Usuario {
    private List<Servicio> suscripciones;

    public Estudiante(String nombre, String codigo, String nombreUsuario, String contrasena) {
        super(nombre, codigo, nombreUsuario, contrasena);
    }

    public void suscribirAServicio(Servicio servicio) {
        suscripciones.add(servicio);
    }

    public boolean estaSuscrito(Servicio servicio) {
        return suscripciones.contains(servicio);
    }


}
