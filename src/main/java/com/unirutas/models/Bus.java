package com.unirutas.models;

import java.util.ArrayList;
import java.util.List;

public class Bus {
    private final String plate_number;
    private final int capacity;
    private final List<Driver> drivers;

    public Bus(int capacity, String plate_number) {
        this.capacity = capacity;
        this.plate_number = plate_number;
        this.drivers = new ArrayList<>();
    }

    public int getCapacity() {
        return capacity;
    }

    public String getPlate() {
        return plate_number;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void assignDriver(Driver driver) {
        drivers.add(driver);
    }

    public void removeDriver(Driver driver) {
        drivers.remove(driver);
    }
}
