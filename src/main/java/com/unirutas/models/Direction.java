package com.unirutas.models;

public class Direction {
    private final String nombre;
    private final Journey recorrido;

    public Direction(String nombre, Journey recorrido) {
        this.nombre = nombre;
        this.recorrido = recorrido;
    }

    public String getNombre() {
        return nombre;
    }
}
