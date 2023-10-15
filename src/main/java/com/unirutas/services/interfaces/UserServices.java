package com.unirutas.services.interfaces;

import java.util.List;

public interface UserServices<T> {
    void create(String name, String code, String username, String password);
    void update(String name, String code, String username, String password);
    void delete(String code);
    T findByCode(String code);
    boolean existsByCode(String code);
    List<T> findAll();

}
