package com.unirutas.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Coordinate {
    private UUID id;
    private double latitude;
    private double longitude;

    public Coordinate(double latitude, double longitude) {
        this.id = UUID.randomUUID();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UUID getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<Double> getCoordinate() {
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(this.latitude);
        coordinates.add(this.longitude);

        return coordinates;
    }
}
