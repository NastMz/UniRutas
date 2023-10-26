package com.unirutas.repository;

import com.unirutas.core.database.repository.CrudRepository;
import com.unirutas.core.dependency.annotations.Repository;
import com.unirutas.models.Service;

@Repository
public class ServiceRepository extends CrudRepository<Service> {
}
