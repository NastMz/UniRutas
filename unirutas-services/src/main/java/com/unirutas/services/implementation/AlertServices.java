package com.unirutas.services.implementation;

import com.flexcore.database.repository.utils.PrimaryKeyValues;
import com.flexcore.dependency.annotations.Inject;
import com.unirutas.models.Alert;
import com.unirutas.repository.AlertRepository;

import java.util.List;
import java.util.Map;

public class AlertServices {
    @Inject
    private AlertRepository alertRepository;

    public void create(Alert alert) {
        alertRepository.save(alert);
    }

    public void update(Alert alert) {
        alertRepository.update(alert);
    }

    public void delete(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        alertRepository.delete(primaryKeyValues);
    }

    public Alert findById(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        return alertRepository.findById(primaryKeyValues);
    }

    public boolean existsById(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        return alertRepository.existsById(primaryKeyValues);
    }

    public List<Alert> findAll() {
        return alertRepository.findAll();
    }
}
