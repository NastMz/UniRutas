package com.unirutas.models;

import java.util.List;

public class Tramo {
    private final List<Coordenada> listadoCoordenadas;

    public Tramo(List<Coordenada> listadoCoordenadas) {
        this.listadoCoordenadas = listadoCoordenadas;
    }

    public void agregarCoordenada(Coordenada coordenada) {
        listadoCoordenadas.add(coordenada);
    }

    public void removerCoordenada(Coordenada coordenada) {
        listadoCoordenadas.remove(coordenada);
    }
}
