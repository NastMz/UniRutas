package com.unirutas.models;

import java.util.UUID;

public class Direction {
    private final UUID id;
    private final String name;

    public Direction(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
