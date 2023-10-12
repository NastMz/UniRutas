package com.unirutas.models;

import java.util.ArrayList;
import java.util.List;

public class Coordinate {
    private final double latitude;
    private final double longitude;

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public List<Double> getCoordinate() {
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(this.latitude);
        coordinates.add(this.longitude);

        return coordinates;
    }
}
