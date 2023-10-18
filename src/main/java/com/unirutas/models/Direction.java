package com.unirutas.models;

import java.util.UUID;

public class Direction {
    private final String id;
    private final String name;

    public Direction(String name) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
