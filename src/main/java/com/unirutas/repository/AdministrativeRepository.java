package com.unirutas.repository;

import com.unirutas.core.database.repository.CrudRepository;
import com.unirutas.core.database.repository.implementation.sql.SQLGenericRepository;
import com.unirutas.core.dependency.annotations.Repository;
import com.unirutas.models.Administrative;

@Repository
public class AdministrativeRepository extends CrudRepository<Administrative> {
}
