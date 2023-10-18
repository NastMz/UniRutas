package com.unirutas.models;

import java.util.UUID;

public class Section {
    private String id;

    public Section() {
        this.id = String.valueOf(UUID.randomUUID());
    }

    public String getId() {
        return id;
    }

    public void addCoordinate(Coordinate coordinate) {
        // Insertar la sección en la tabla JourneySection
        SectionCoordinate.insertCoordinate(this.id, coordinate.getId());
    }

    public void removeCoordinate(Coordinate coordinate) {
        // Eliminar la sección de la tabla JourneySection
        SectionCoordinate.removeCoordinate(this.id, coordinate.getId());
    }
}
