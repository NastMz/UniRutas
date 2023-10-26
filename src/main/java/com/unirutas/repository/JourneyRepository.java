package com.unirutas.repository;

import com.unirutas.core.database.repository.CrudRepository;
import com.unirutas.core.dependency.annotations.Repository;
import com.unirutas.models.Journey;

@Repository
public class JourneyRepository extends CrudRepository<Journey> {
}
