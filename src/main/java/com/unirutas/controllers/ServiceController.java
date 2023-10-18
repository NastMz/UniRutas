package com.unirutas.controllers;

import com.unirutas.models.*;

import java.util.List;

public class ServiceController {
    private final Service service;

    public ServiceController(Route route, Direction direction) {
        service = new Service(route.getId(), direction.getId());
    }

    public void assignSchedule(Schedule schedule) {
        this.service.addSchedule(schedule);
    }

    public void assignBus(Bus bus) {
        service.addBus(bus);
    }

    public void removeSchedule(Schedule schedule) {
        service.removeSchedule(schedule);
    }

    public void removeBus(Bus bus) {
        service.removeBus(bus);
    }

    public List<Bus> listBuses() {
        return service.getBuses();
    }

    public List<Schedule> listSchedules() {
        return service.getSchedules();
    }

    public void addService(Service service) {
        // TODO: Implementar la lógica para añadirlo en la DB
    }

    public void removeService(Service service) {
        // TODO: Implementar la lógica para añadirlo en la DB
    }
}
