package com.unirutas.models;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private final List<Schedule> listadoHorarios;
    private final List<Bus> listadoBuses;
    private final List<Alert> listadoAlertas;
    private final Route ruta;
    private final Direction sentido;

    public Service(Route ruta, Direction sentido) {
        this.listadoHorarios = new ArrayList<>();
        this.listadoBuses = new ArrayList<>();
        this.listadoAlertas = new ArrayList<>();
        this.ruta = ruta;
        this.sentido = sentido;
    }

    public List<Schedule> getSchedules() {
        return this.listadoHorarios;
    }

    public List<Bus> getBuses() {
        return this.listadoBuses;
    }

    public List<Alert> getAlertas() {
        return this.listadoAlertas;
    }

    public void addBus(Bus bus) {
        listadoBuses.add(bus);
    }

    public void addSchedule(Schedule horario) {
        listadoHorarios.add(horario);
    }

    public void añadirAlerta(Alert alerta) {
        listadoAlertas.add(alerta);
    }

    public void removeBus(Bus bus) {
        listadoBuses.remove(bus);
    }

    public void removeSchedule(Schedule horario) {
        listadoHorarios.remove(horario);
    }

    public void removerAlerta(Alert alerta) {
        listadoAlertas.remove(alerta);
    }
}
