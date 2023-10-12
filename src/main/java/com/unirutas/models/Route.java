package com.unirutas.models;

public class Route {
    private final String name;
    private final Journey journey;

    public Route(String name, Journey journey) {
        this.name = name;
        this.journey = journey;
    }

    public String getName() {
        return name;
    }
}

