package com.unirutas.controllers;

import com.flexcore.dependency.annotations.Inject;
import com.unirutas.models.Direction;
import com.unirutas.services.implementation.DirectionServices;

import java.util.List;

public class DirectionController {
    @Inject
    private DirectionServices directionServices;
    public void createDirection(Direction direction) {
        directionServices.create(direction);
    }

    public void updateDirection(Direction direction) {
        directionServices.update(direction);
    }

    public void deleteDirection(String id) {
        directionServices.delete(id);
    }

    public Direction findDirectionById(String id) {
        return directionServices.findById(id);
    }

    public boolean existsDirectionById(String id) {
        return directionServices.existsById(id);
    }

    public List<Direction> findAllDirections() {
        return directionServices.findAll();
    }

}
