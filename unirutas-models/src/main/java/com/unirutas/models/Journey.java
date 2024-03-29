package com.unirutas.models;

import com.flexcore.annotations.Column;
import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

import java.util.List;
import java.util.UUID;

@Table(name="Journey")
public class Journey {
    @PrimaryKey(name = "id")
    private String id;
    @Column(name = "direction_id")
    private String directionId;

    public Journey(String directionId) {
        this.id = String.valueOf(UUID.randomUUID());
        this.directionId = directionId;
    }

    public String getId() {
        return id;
    }

    public List<Stop> getStopsList() {
        // Obtener la lista de paradas asociadas a este viaje desde la base de datos
        return JourneyStop.getStopsForJourney(this.id);
    }

    public List<Section> getSectionsList() {
        // Obtener la lista de secciones asociadas a este viaje desde la base de datos
        return JourneySection.getSectionsForJourney(this.id);
    }

    public void addStop(Stop stop) {
        // Insertar la parada en la tabla JourneyStop
        JourneyStop.insertStop(this.id, stop.getId());
    }

    public void removeStop(Stop stop) {
        // Eliminar la parada de la tabla JourneyStop
        JourneyStop.removeStop(this.id, stop.getId());
    }

    public void addSection(Section section) {
        // Insertar la sección en la tabla JourneySection
        JourneySection.insertSection(this.id, section.getId());
    }

    public void removeSection(Section section) {
        // Eliminar la sección de la tabla JourneySection
        JourneySection.removeSection(this.id, section.getId());
    }
}
