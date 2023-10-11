package com.unirutas.models;

import java.util.List;

public class Paradero {
    private final String nombre;
    private final String descripcion;
    private Tramo tramoAnterior;
    private Tramo tramoSiguiente;
    private final Coordenada coordenada;

    public Paradero(String nombre, String descripcion, Coordenada coordenada) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.coordenada = coordenada;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void asignarTramoAnterior(Tramo tramo) {
        this.tramoAnterior = tramo;
    }

    public void asignarTramoSiguiente(Tramo tramo) {
        this.tramoSiguiente = tramo;
    }

    public List<Double> getCoordenada() {
        return coordenada.getCoordenada();
    }
}
