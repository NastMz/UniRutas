package com.unirutas.models;

import com.unirutas.repository.GenericDTO;

import java.util.List;

public class Administrative extends User {

    public Administrative(String name, String code, String username, String password) {
        super(name, code, username, password);
    }

    @Override
    public void insert(User user) {
        GenericDTO<Administrative> administrativeDTO = new GenericDTO<>(Administrative.class, "Administrative", List.of("code"));
        administrativeDTO.insert((Administrative) user);
    }

    @Override
    public void update(User user) {
        GenericDTO<Administrative> administrativeDTO = new GenericDTO<>(Administrative.class, "Administrative", List.of("code"));
        administrativeDTO.update((Administrative) user);
    }
}

