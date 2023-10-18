package com.unirutas.services.implementation;

import com.unirutas.core.database.repository.interfaces.IRepository;
import com.unirutas.core.database.repository.utils.PrimaryKeyValues;
import com.unirutas.core.providers.RepositoryFactoryProvider;
import com.unirutas.models.Administrative;
import com.unirutas.services.interfaces.UserServices;

import java.util.List;
import java.util.Map;

public class AdministrativeServices implements UserServices<Administrative> {
    private final IRepository<Administrative> administrativeRepository;

    public AdministrativeServices() {
        this.administrativeRepository = RepositoryFactoryProvider.getFactory().createRepository(Administrative.class);
        administrativeRepository.getDatabaseEngineDate();
        administrativeRepository.getDatabaseEngineHour();
    }

    public void create(String name, String code, String username, String password) {
        administrativeRepository.save(new Administrative(name, code, username, password));
    }

    public void update(String name, String code, String username, String password) {
        administrativeRepository.update(new Administrative(name, code, username, password));
    }

    public void delete(String code) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("code", code));
        administrativeRepository.delete(primaryKeyValues);
    }

    public Administrative findByCode(String code) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("code", code));
        return administrativeRepository.findById(primaryKeyValues);
    }

    public boolean existsByCode(String code) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("code", code));
        return administrativeRepository.existsById(primaryKeyValues);
    }

    public List<Administrative> findAll() {
        return administrativeRepository.findAll();
    }
}
