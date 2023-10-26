package com.unirutas.controllers;

import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.models.Journey;
import com.unirutas.services.implementation.JourneyServices;

import java.util.List;

public class JourneyController {
    @Inject
    private JourneyServices journeyServices;
    public void createJourney(Journey journey) {
        journeyServices.create(journey);
    }

    public void updateJourney(Journey journey) {
        journeyServices.update(journey);
    }

    public void deleteJourney(String id) {
        journeyServices.delete(id);
    }

    public Journey findJourneyById(String id) {
        return journeyServices.findById(id);
    }

    public boolean existsJourneyById(String id) {
        return journeyServices.existsById(id);
    }

    public List<Journey> findAllJourneys() {
        return journeyServices.findAll();
    }

}
