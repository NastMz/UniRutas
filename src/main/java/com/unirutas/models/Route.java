package com.unirutas.models;

import java.util.UUID;

public class Route {
    private final UUID id;
    private final String name;
    private final UUID journeyId;

    public Route(String name, UUID journeyId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.journeyId = journeyId;
    }

    public String getName() {
        return name;
    }
}
