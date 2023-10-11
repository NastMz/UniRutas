package com.unirutas.models;

public class Sentido {
    private final String nombre;
    private final Recorrido recorrido;

    public Sentido(String nombre, Recorrido recorrido) {
        this.nombre = nombre;
        this.recorrido = recorrido;
    }

    public String getNombre() {
        return nombre;
    }
}
