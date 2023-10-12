package com.unirutas.models;

import java.util.List;

public class Stop {
    private final String name;
    private final String description;
    private Section beforeSection;
    private Section nextSection;
    private final Coordinate coordinate;

    public Stop(String name, String description, Coordinate coordinate) {
        this.name = name;
        this.description = description;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void assignBeforeSection(Section section) {
        this.beforeSection = section;
    }

    public void assignNextSection(Section section) {
        this.nextSection = section;
    }

    public List<Double> getCoordinate() {
        return coordinate.getCoordinate();
    }
}
