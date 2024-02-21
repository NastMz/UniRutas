package com.unirutas.models;

import com.flexcore.annotations.Column;
import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name="Coordinate")
public class Coordinate {
    @PrimaryKey(name = "id")
    private String id;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;

    public Coordinate(double latitude, double longitude) {
        this.id = String.valueOf(UUID.randomUUID());
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
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
