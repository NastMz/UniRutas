package com.unirutas.models;

import java.util.List;

public class Recorrido {
    private Sentido sentido;
    private List<Paradero> listadoParaderos;
    private List<Tramo> listadoTramos;

    public Recorrido(List<Paradero> listadoParaderos, List<Tramo> listadoTramos) {
        this.listadoParaderos = listadoParaderos;
        this.listadoTramos = listadoTramos;
    }


    public List<Paradero> getListadoParaderos() {
        return listadoParaderos;
    }

    public void setListadoParaderos(List<Paradero> listadoParaderos) {
        this.listadoParaderos = listadoParaderos;
    }

    public List<Tramo> getListadoTramos() {
        return listadoTramos;
    }

    public void setListadoTramos(List<Tramo> listadoTramos) {
        this.listadoTramos = listadoTramos;
    }


    public void agregarParadero(Paradero paradero) {
        listadoParaderos.add(paradero);
    }

    public void agregarTramo(Tramo tramo) {
        listadoTramos.add(tramo);
    }

    public void removerParadero(Paradero paradero) {
        listadoParaderos.remove(paradero);
    }

    public void removerTramo(Tramo tramo) {
        listadoTramos.remove(tramo);
    }
}
