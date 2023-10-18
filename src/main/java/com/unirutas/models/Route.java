package com.unirutas.models;

import java.util.UUID;
import com.unirutas.core.annotations.Column;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;

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
