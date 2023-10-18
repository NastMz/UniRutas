package com.unirutas.models;

import java.util.List;

public class JourneyStop {
    private String journeyId;
    private String stopId;



    public JourneyStop(String journeyId, String stopId) {
        this.journeyId = journeyId;
        this.stopId = stopId;
    }

    public static List<Stop> getStopsForJourney(String journeyId) {
        // Obtener y retornar la lista de paradas asociadas a un viaje desde la base de datos
        // TODO: Implementa esta lógica
        return null;
    }

    public static void insertStop(String journeyId, String stopId) {
        // Insertar la parada en la tabla JourneyStop
        // TODO: Implementa esta lógica
    }

    public static void removeStop(String journeyId, String stopId) {
        // Eliminar la parada de la tabla JourneyStop
        // TODO: Implementa esta lógica
    }
}
