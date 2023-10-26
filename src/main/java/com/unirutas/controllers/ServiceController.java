package com.unirutas.controllers;

import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.models.*;
import com.unirutas.services.implementation.ServiceServices;

import java.util.List;

public class ServiceController {
    // TODO: Quitar el atributo service y pasarlo por parámetro en los métodos
    private final Service service;

    @Inject
    private ServiceServices serviceServices;

    public ServiceController(Route route) {
        service = new Service(route.getId());
    }

    public void assignSchedule(Schedule schedule) {
        // TODO: Implementar el método con la lógica de negocio correspondiente
        this.service.addSchedule(schedule);
    }

    public void assignBus(Bus bus) {
        // TODO: Implementar el método con la lógica de negocio correspondiente
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
        serviceServices.create(service);
    }

    public void updateService(Service service) {
        serviceServices.update(service);
    }

    public void removeService(Service service) {
        serviceServices.delete(service.getId());
    }

    public Service findServiceById(String id) {
        return serviceServices.findById(id);
    }

    public boolean existsServiceById(String id) {
        return serviceServices.existsById(id);
    }

    public List<Service> findAllServices() {
        return serviceServices.findAll();
    }
}
