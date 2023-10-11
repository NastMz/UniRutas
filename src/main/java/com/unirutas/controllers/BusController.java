package com.unirutas.controllers;

import com.unirutas.models.Bus;
import com.unirutas.models.Conductor;

public class BusController {

    public void agregarBus(Bus bus) {
        // TODO: Implementar la lógica para añadirlo en la DB
    }

    public void eliminarBus(Bus bus) {
        // TODO: Implementar la lógica para añadirlo en la DB
    }

    public void getBusInfo(Bus bus) {
        // TODO: Implementar la lógica para añadirlo en la DB
        System.out.println("Placa: " + bus.getPlaca());
        System.out.println("Capacidad: " + bus.getCapacidad());
        System.out.println("Conductores: ");
        for (Conductor conductor : bus.getConductores()) {
            System.out.println("- Nombre: " + conductor.getNombre());
            System.out.println("  Documento: " + conductor.getCodigo());
        }
    }

    public void actualizarBus(Bus bus) {
        //TODO: Implementar la lógica para añadirlo en la DB
    }
}
