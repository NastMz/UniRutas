package com.unirutas.controllers;

import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.models.Route;
import com.unirutas.models.User;
import com.unirutas.services.implementation.RouteServices;

import java.util.List;

public class RouteController {
    @Inject
    private RouteServices routeServices;
    public void createRoute(Route route) {
        routeServices.create(route);
    }

    public void updateRoute(Route route) {
        routeServices.update(route);
    }

    public void deleteRoute(String id) {
        routeServices.delete(id);
    }

    public Route findRouteById(String id) {
        return routeServices.findById(id);
    }

    public boolean existsRouteById(String id) {
        return routeServices.existsById(id);
    }

    public List<Route> findAllRoutes() {
        return routeServices.findAll();
    }

}
