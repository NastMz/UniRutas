package com.unirutas.models;

import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

import java.util.List;

@Table(name="DriverBus")
public class DriverBus {
    @PrimaryKey(name = "document_number")
    private String documentNumber;
    @PrimaryKey(name = "bus_id")
    private String busId;

    public DriverBus(String documentNumber, String busId) {
        this.documentNumber = documentNumber;
        this.busId = busId;
    }

    public static List<Driver> getDriversForBus(String busId) {
        // TODO: Implementa esta lógica
        return null;
    }

    public static void assignDriver(String documentNumber, String busId) {
        // TODO: Implementa esta lógica
    }

    public static void removeDriver(String documentNumber, String busId) {
        // TODO: Implementa esta lógica
    }
}
