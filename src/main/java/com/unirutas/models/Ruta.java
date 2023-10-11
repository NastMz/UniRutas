package com.unirutas.models;

public class Ruta {
    private final String nombre;
    private final Recorrido recorrido;

    public Ruta(String nombre, Recorrido recorrido) {
        this.nombre = nombre;
        this.recorrido = recorrido;
    }

    public String getNombre() {
        return nombre;
    }
}

