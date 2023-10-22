package com.unirutas.services.implementation;

import com.unirutas.core.database.repository.interfaces.IRepository;
import com.unirutas.core.database.repository.utils.PrimaryKeyValues;
import com.unirutas.core.dependency.annotations.Implementation;
import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.core.providers.RepositoryFactoryProvider;
import com.unirutas.models.Administrative;
import com.unirutas.models.User;
import com.unirutas.repository.AdministrativeRepository;
import com.unirutas.services.interfaces.UserServices;

import java.util.List;
import java.util.Map;

@Implementation
public class AdministrativeServices implements UserServices<Administrative> {
    @Inject
    private AdministrativeRepository administrativeRepository;

    public void create(User user) {
        administrativeRepository.save((Administrative) user);
    }

    public void update(User user) {
        administrativeRepository.update((Administrative) user);
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
