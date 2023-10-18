package com.unirutas.models;

import java.util.List;
import java.util.UUID;

public class JourneySection {
    private UUID journeyId;
    private UUID sectionId;

    public JourneySection(UUID journeyId, UUID sectionId) {
        this.journeyId = journeyId;
        this.sectionId = sectionId;
    }

    public static List<Section> getSectionsForJourney(UUID journeyId) {
        // Obtener y retornar la lista de secciones asociadas a un viaje desde la base de datos
        // TODO: Implementa esta lógica
        return null;
    }

    public static void insertSection(UUID journeyId, UUID sectionId) {
        // Insertar la sección en la tabla JourneySection
        // TODO: Implementa esta lógica
    }

    public static void removeSection(UUID journeyId, UUID sectionId) {
        // Eliminar la sección de la tabla JourneySection
        // TODO: Implementa esta lógica
    }
}
