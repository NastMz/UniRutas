package com.unirutas.models;

import java.util.List;
import java.util.UUID;

public class SectionCoordinate {
    private UUID sectionId;
    private UUID coordinateId;

    public SectionCoordinate(UUID sectionId, UUID coordinateId) {
        this.sectionId = sectionId;
        this.coordinateId = coordinateId;
    }

    public static List<SectionCoordinate> getCoordinateForSection(UUID sectionId) {
        // TODO: Lógica para obtener la lista de relaciones desde la base de datos
        return null;
    }

    public static void insertCoordinate(UUID sectionId, UUID coordinateId) {
        // TODO: Lógica para agregar la relación en la base de datos
    }

    public static void removeCoordinate(UUID sectionId, UUID coordinateId) {
        // TODO: Lógica para eliminar la relación en la base de datos
    }
}
