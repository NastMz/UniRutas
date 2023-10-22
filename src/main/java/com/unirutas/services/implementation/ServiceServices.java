package com.unirutas.services.implementation;

import com.unirutas.core.database.repository.utils.PrimaryKeyValues;
import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.models.Service;
import com.unirutas.repository.ServiceRepository;

import java.util.List;
import java.util.Map;

public class ServiceServices {
    @Inject
    private ServiceRepository serviceRepository;

    public void create(Service service) {
        serviceRepository.save(service);
    }

    public void update(Service service) {
        serviceRepository.update(service);
    }

    public void delete(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        serviceRepository.delete(primaryKeyValues);
    }

    public Service findById(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        return serviceRepository.findById(primaryKeyValues);
    }

    public boolean existsById(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        return serviceRepository.existsById(primaryKeyValues);
    }

    public List<Service> findAll() {
        return serviceRepository.findAll();
    }
}
