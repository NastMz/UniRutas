package com.unirutas.models;

import com.flexcore.annotations.Column;
import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

import java.util.UUID;

@Table(name="Route")
public class Route {
    @PrimaryKey(name = "id")
    private final String id;
    @Column(name = "name")
    private final String name;
    @Column(name = "journey_id")
    private final String journeyId;

    public Route(String name, String journeyId) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.journeyId = journeyId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
