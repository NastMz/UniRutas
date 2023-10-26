package com.unirutas.repository;

import com.unirutas.core.database.repository.CrudRepository;
import com.unirutas.core.dependency.annotations.Repository;
import com.unirutas.models.Direction;

@Repository
public class DirectionRepository extends CrudRepository<Direction> {
}
