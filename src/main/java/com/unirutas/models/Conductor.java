package com.unirutas.models;

import java.util.List;

public class Conductor extends Persona {
    private List<Bus> conductores;

    public Conductor(String nombre, String codigo) {
        super(nombre, codigo);
    }
}
