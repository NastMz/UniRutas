package com.unirutas.models;

import java.util.UUID;

public class Section {
    private UUID id;

    public Section() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
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
