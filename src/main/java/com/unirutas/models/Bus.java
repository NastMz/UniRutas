package com.unirutas.models;

import java.util.List;
import java.util.UUID;
import com.unirutas.core.annotations.Column;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;

@Table(name="Bus")
public class Bus {
    @PrimaryKey(name = "id")
    private String id;
    @Column(name = "plate_number")
    private final String plateNumber;
    @Column(name = "capacity")
    private final int capacity;

    public Bus(int capacity, String plateNumber) {
        this.id = String.valueOf(UUID.randomUUID());
        this.capacity = capacity;
        this.plateNumber = plateNumber;
    }

    public String getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getPlate() {
        return plateNumber;
    }

    public List<Driver> getDrivers() {
        // Obtener la lista de paradas asociadas a este viaje desde la base de datos
        return DriverBus.getDriversForBus(this.id);
    }

    public void addDriver(Driver driver) {
        // TODO:
        DriverBus.assignDriver(this.id, driver.getCode());
    }

    public void removeDriver(Driver driver) {
        // TODO:
        DriverBus.removeDriver(this.id, driver.getCode());
    }
}
