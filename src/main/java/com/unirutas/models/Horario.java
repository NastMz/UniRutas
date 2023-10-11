package com.unirutas.models;

import java.time.LocalTime;

public class Horario {
    private final LocalTime hora;

    public Horario(LocalTime hora) {
        this.hora = hora;
    }

    public LocalTime getHora() {
        return hora;
    }
}
