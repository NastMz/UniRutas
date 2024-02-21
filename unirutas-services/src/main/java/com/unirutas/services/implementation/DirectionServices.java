package com.unirutas.services.implementation;

import com.flexcore.database.repository.utils.PrimaryKeyValues;
import com.flexcore.dependency.annotations.Inject;
import com.unirutas.models.Direction;
import com.unirutas.repository.DirectionRepository;

import java.util.List;
import java.util.Map;

public class DirectionServices {
    @Inject
    private DirectionRepository directionRepository;

    public void create(Direction direction) {
        directionRepository.save(direction);
    }

    public void update(Direction direction) {
        directionRepository.update(direction);
    }

    public void delete(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        directionRepository.delete(primaryKeyValues);
    }

    public Direction findById(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        return directionRepository.findById(primaryKeyValues);
    }

    public boolean existsById(String id) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("id", id));
        return directionRepository.existsById(primaryKeyValues);
    }

    public List<Direction> findAll() {
        return directionRepository.findAll();
    }
}
