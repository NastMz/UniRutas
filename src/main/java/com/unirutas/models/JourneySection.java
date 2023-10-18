package com.unirutas.models;

import java.util.List;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;

@Table(name="JourneySection")
public class JourneySection {
    @PrimaryKey(name = "journey_id")
    private String journeyId;
    @PrimaryKey(name = "section_id")
    private String sectionId;

    public JourneySection(String journeyId, String sectionId) {
        this.journeyId = journeyId;
        this.sectionId = sectionId;
    }

    public static List<Section> getSectionsForJourney(String journeyId) {
        // Obtener y retornar la lista de secciones asociadas a un viaje desde la base de datos
        // TODO: Implementa esta lógica
        return null;
    }

    public static void insertSection(String journeyId, String sectionId) {
        // Insertar la sección en la tabla JourneySection
        // TODO: Implementa esta lógica
    }

    public static void removeSection(String journeyId, String sectionId) {
        // Eliminar la sección de la tabla JourneySection
        // TODO: Implementa esta lógica
    }
}
