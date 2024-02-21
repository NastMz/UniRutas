package com.unirutas.repository;

import com.flexcore.database.repository.CrudRepository;
import com.flexcore.dependency.annotations.Repository;
import com.unirutas.models.Direction;

@Repository
public class DirectionRepository extends CrudRepository<Direction> {
}
