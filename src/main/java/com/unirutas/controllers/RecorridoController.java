package com.unirutas.controllers;

import com.unirutas.models.Coordenada;
import com.unirutas.models.Paradero;
import com.unirutas.models.Recorrido;
import com.unirutas.models.Tramo;

import java.util.List;


// TODO: Implementar la lógica para añadirlo en la DB
public class RecorridoController {
    private Recorrido recorrido;

    public void agregarParadero(Paradero paradero) {
        recorrido.agregarParadero(paradero);
    }

    public void eliminarParadero(Paradero paradero) {
        recorrido.removerParadero(paradero);
    }

    public void asignarParaderos(List<Paradero> paraderos) {
        recorrido.setListadoParaderos(paraderos);
    }

    public void crearTramo(List<Coordenada> coordenadas) {
        Tramo tramo = new Tramo(coordenadas);
        recorrido.agregarTramo(tramo);
    }

    public void eliminarTramo(Tramo tramo) {
        recorrido.removerTramo(tramo);
    }

    public void asignarTramos(List<Tramo> tramos) {
        recorrido.setListadoTramos(tramos);
    }

    public void eliminarTramos(List<Tramo> tramos) {
        for (Tramo tramo : tramos) {
            recorrido.removerTramo(tramo);
        }
    }
}
