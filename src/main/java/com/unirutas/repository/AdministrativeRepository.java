package com.unirutas.repository;

import com.unirutas.core.database.repository.implementation.sql.SQLGenericRepository;
import com.unirutas.models.Administrative;

public class AdministrativeRepository extends SQLGenericRepository<Administrative> {
    public AdministrativeRepository() {
        super(Administrative.class);
    }
}
