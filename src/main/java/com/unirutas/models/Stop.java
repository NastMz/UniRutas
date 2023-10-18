package com.unirutas.models;

import java.util.List;
import java.util.UUID;

@Table(name="Stop")
public class Stop {
    @PrimaryKey(name = "id")
    private String id;
    @Column(name = "name")
    private final String name;
    @Column(name = "description")
    private final String description;
    @Column(name = "previous_section")
    private Section previousSection;
    @Column(name = "next_section")
    private Section nextSection;

    // TODO: ojito con la llave foranea
    @Column(name = "coordinate_id")
    private final String coordinateId;

    public Stop(String name, String description, String coordinateId) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.description = description;
        this.coordinateId = coordinateId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void assignBeforeSection(Section section) {
        this.previousSection = section;
    }

    public void assignNextSection(Section section) {
        this.nextSection = section;
    }

    public List<Double> getCoordinate(Coordinate coordinate) {
        return coordinate.getCoordinate();
    }
}
