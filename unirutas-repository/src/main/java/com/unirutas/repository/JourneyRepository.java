package com.unirutas.repository;

import com.flexcore.database.repository.CrudRepository;
import com.flexcore.dependency.annotations.Repository;
import com.unirutas.models.Journey;

@Repository
public class JourneyRepository extends CrudRepository<Journey> {
}
