package com.unirutas.models;

import java.util.UUID;

public class Route {
    private final String id;
    private final String name;
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
