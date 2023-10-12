package com.unirutas.models;

import java.util.List;

public class Section {
    private final List<Coordinate> listadoCoordenadas;

    public Section(List<Coordinate> listadoCoordenadas) {
        this.listadoCoordenadas = listadoCoordenadas;
    }

    public void agregarCoordenada(Coordinate coordenada) {
        listadoCoordenadas.add(coordenada);
    }

    public void removerCoordenada(Coordinate coordenada) {
        listadoCoordenadas.remove(coordenada);
    }
}
