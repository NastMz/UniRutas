package com.unirutas.controllers;

import com.unirutas.models.Bus;
import com.unirutas.models.Driver;

public class BusController {

    public void addBus(Bus bus) {
        // TODO: Implementar la lógica para añadirlo en la DB
    }

    public void removeBus(Bus bus) {
        // TODO: Implementar la lógica para añadirlo en la DB
    }

    public void getBusInfo(Bus bus) {
        // TODO: Implementar la lógica para añadirlo en la DB
        if (bus.getDrivers() != null) {
            System.out.println("Placa: " + bus.getPlate());
            System.out.println("Capacidad: " + bus.getCapacity());
            System.out.println("Conductores: ");
            for (Driver driver : bus.getDrivers()) {
                System.out.println("- Nombre: " + driver.getName());
                System.out.println("  Documento: " + driver.getCode());
            }
        } else {
            System.out.println("El autobús no tiene conductores asignados.");
        }
    }

    public void updateBus(Bus bus) {
        //TODO: Implementar la lógica para añadirlo en la DB
    }
}
