package com.unirutas.services.implementation;

import com.unirutas.core.database.repository.utils.PrimaryKeyValues;
import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.models.Journey;
import com.unirutas.repository.JourneyRepository;

import java.util.List;
import java.util.Map;

public class JourneyServices {
    @Inject
    private JourneyRepository journeyRepository;

    public void create(Journey journey) {
        journeyRepository.save(journey);
    }

    public void update(Journey journey) {
        journeyRepository.update(journey);
    }

    public void delete(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        journeyRepository.delete(primaryKeyValues);
    }

    public Journey findById(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        return journeyRepository.findById(primaryKeyValues);
    }

    public boolean existsById(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        return journeyRepository.existsById(primaryKeyValues);
    }

    public List<Journey> findAll() {
        return journeyRepository.findAll();
    }
}
