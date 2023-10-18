package com.unirutas.models;

import java.util.UUID;

@Table(name="Direction")
public class Direction {
    @PrimaryKey(name = "id")
    private final String id;
    @Column(name = "name")
    private final String name;

    public Direction(String name) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
