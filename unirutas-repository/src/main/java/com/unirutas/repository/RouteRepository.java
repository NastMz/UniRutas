package com.unirutas.repository;

import com.flexcore.database.repository.CrudRepository;
import com.flexcore.dependency.annotations.Repository;
import com.unirutas.models.Route;

@Repository
public class RouteRepository extends CrudRepository<Route> {
}
