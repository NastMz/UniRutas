package com.unirutas.models;

import java.util.UUID;
import com.unirutas.core.annotations.Column;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;

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
