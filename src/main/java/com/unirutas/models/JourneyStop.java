package com.unirutas.models;

import java.util.List;
import java.util.UUID;

public class JourneyStop {
    private UUID journeyId;
    private UUID stopId;



    public JourneyStop(UUID journeyId, UUID stopId) {
        this.journeyId = journeyId;
        this.stopId = stopId;
    }

    public static List<Stop> getStopsForJourney(UUID journeyId) {
        // Obtener y retornar la lista de paradas asociadas a un viaje desde la base de datos
        // TODO: Implementa esta lógica
        return null;
    }

    public static void insertStop(UUID journeyId, UUID stopId) {
        // Insertar la parada en la tabla JourneyStop
        // TODO: Implementa esta lógica
    }

    public static void removeStop(UUID journeyId, UUID stopId) {
        // Eliminar la parada de la tabla JourneyStop
        // TODO: Implementa esta lógica
    }
}
