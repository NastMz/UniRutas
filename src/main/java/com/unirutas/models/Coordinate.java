package com.unirutas.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.unirutas.core.annotations.Column;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;

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
