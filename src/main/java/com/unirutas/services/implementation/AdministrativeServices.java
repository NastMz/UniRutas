package com.unirutas.services.implementation;

import com.unirutas.models.Administrative;
import com.unirutas.repository.implementation.AdministrativeRepository;
import com.unirutas.repository.interfaces.IRepository;
import com.unirutas.services.interfaces.UserServices;

import java.util.List;

public class AdministrativeServices implements UserServices<Administrative> {
    private final IRepository<Administrative, Object> administrativeRepository = new AdministrativeRepository();

    public void create(String name, String code, String username, String password) {
        administrativeRepository.save(new Administrative(name, code, username, password));
    }

    public void update(String name, String code, String username, String password) {
        administrativeRepository.update(new Administrative(name, code, username, password));
    }

    public void delete(String code) {
        administrativeRepository.delete(code);
    }

    public Administrative findByCode(String code) {
        return administrativeRepository.findById(code);
    }

    public boolean existsByCode(String code) {
        return administrativeRepository.existsById(code);
    }

    public List<Administrative> findAll() {
        return administrativeRepository.findAll();
    }
}
