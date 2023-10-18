package com.unirutas.models;

import java.util.List;

public class SectionCoordinate {
    private String sectionId;
    private String coordinateId;

    public SectionCoordinate(String sectionId, String coordinateId) {
        this.sectionId = sectionId;
        this.coordinateId = coordinateId;
    }

    public static List<SectionCoordinate> getCoordinateForSection(String sectionId) {
        // TODO: Lógica para obtener la lista de relaciones desde la base de datos
        return null;
    }

    public static void insertCoordinate(String sectionId, String coordinateId) {
        // TODO: Lógica para agregar la relación en la base de datos
    }

    public static void removeCoordinate(String sectionId, String coordinateId) {
        // TODO: Lógica para eliminar la relación en la base de datos
    }
}
