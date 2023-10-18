package com.unirutas.models;

import java.util.UUID;

@Table(name="Route")
public class Route {
    @PrimaryKey(name = "id")
    private final String id;
    @Column(name = "name")
    private final String name;

    // TODO: Revisar las anotaciones
    @Column(name = "journey_id")
    private final String journeyId;

    public Route(String name, String journeyId) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.journeyId = journeyId;
    }

    public String getName() {
        return name;
    }
}
