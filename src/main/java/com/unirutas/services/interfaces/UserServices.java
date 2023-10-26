package com.unirutas.services.interfaces;

import com.unirutas.models.User;

import java.util.List;

public interface UserServices<T> {
    void create(User user);
    void update(User user);
    void delete(String code);
    T findByCode(String code);
    boolean existsByCode(String code);
    List<T> findAll();

}
