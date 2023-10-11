package com.unirutas.models;

import java.util.ArrayList;
import java.util.List;

public class Bus {
    private final int capacidad;
    private final String placa;
    private final List<Conductor> conductores;

    public Bus(int capacidad, String placa) {
        this.capacidad = capacidad;
        this.placa = placa;
        this.conductores = new ArrayList<>();
    }

    public int getCapacidad() {
        return capacidad;
    }

    public String getPlaca() {
        return placa;
    }

    public List<Conductor> getConductores() {
        return conductores;
    }

    public void asignarConductor(Conductor conductor) {
        conductores.add(conductor);
    }

    public void removerConductor(Conductor conductor) {
        conductores.remove(conductor);
    }
}
