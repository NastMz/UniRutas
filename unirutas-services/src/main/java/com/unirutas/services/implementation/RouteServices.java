package com.unirutas.services.implementation;

import com.flexcore.database.repository.utils.PrimaryKeyValues;
import com.flexcore.dependency.annotations.Inject;
import com.unirutas.models.Route;
import com.unirutas.repository.RouteRepository;

import java.util.List;
import java.util.Map;

public class RouteServices {
    @Inject
    private RouteRepository routeRepository;

    public void create(Route route) {
        routeRepository.save(route);
    }

    public void update(Route route) {
        routeRepository.update(route);
    }

    public void delete(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        routeRepository.delete(primaryKeyValues);
    }

    public Route findById(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        return routeRepository.findById(primaryKeyValues);
    }

    public boolean existsById(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        return routeRepository.existsById(primaryKeyValues);
    }

    public List<Route> findAll() {
        return routeRepository.findAll();
    }
}
