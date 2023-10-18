package com.unirutas.repository.interfaces;

import java.util.List;

public interface IRepository<T, I> {
    T findById(I id);
    List<T> findAll();
    void save(T entity);
    void delete(I id);

    void update(T entity);
    boolean existsById(I... id);
}
