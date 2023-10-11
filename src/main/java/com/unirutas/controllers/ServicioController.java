package com.unirutas.controllers;

import com.unirutas.models.*;

import java.util.List;

public class ServicioController {
    private final Servicio servicio;

    public ServicioController(Ruta ruta, Sentido sentido) {
        servicio = new Servicio(ruta, sentido);
    }

    public void asignarHorario(Horario horario) {
        this.servicio.añadirHorario(horario);
    }

    public void asignarBus(Bus bus) {
        servicio.añadirBus(bus);
    }

    public void removerHorario(Horario horario) {
        servicio.removerHorario(horario);
    }

    public void removerBus(Bus bus) {
        servicio.removerBus(bus);
    }

    public List<Bus> listarBuses() {
        return servicio.getBuses();
    }

    public List<Horario> listarHorarios() {
        return servicio.getHorarios();
    }

    public void añadirServicio(Servicio servicio) {
        // TODO: Implementar la lógica para añadirlo en la DB
    }

    public void eliminarServicio(Servicio servicio) {
        // TODO: Implementar la lógica para añadirlo en la DB
    }
}
