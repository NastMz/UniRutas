package com.unirutas.models;

import com.flexcore.annotations.Column;
import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
