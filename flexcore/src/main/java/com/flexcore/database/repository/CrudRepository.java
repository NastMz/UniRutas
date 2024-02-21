package com.flexcore.database.repository;

import com.flexcore.database.repository.interfaces.IRepository;
import com.flexcore.database.repository.utils.PrimaryKeyValues;
import com.flexcore.providers.RepositoryFactoryProvider;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * CrudRepository class. This class can be used to create a repository. It is used to create a repository of a specific entity.
 * @param <T> The entity class.
 */
public class CrudRepository<T> implements IRepository<T> {
    private final IRepository<T> repository;

    public CrudRepository() {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        repository = RepositoryFactoryProvider.getFactory().createRepository(entityClass);
    }

    @Override
    public void getDatabaseEngineDate() {
        repository.getDatabaseEngineDate();
    }

    @Override
    public void getDatabaseEngineHour() {
        repository.getDatabaseEngineHour();
    }

    @Override
    public T findById(PrimaryKeyValues idValues) {
        return repository.findById(idValues);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(T entity) {
        repository.save(entity);
    }

    @Override
    public void delete(PrimaryKeyValues idValues) {
        repository.delete(idValues);
    }

    @Override
    public void update(T entity) {
        repository.update(entity);
    }

    @Override
    public boolean existsById(PrimaryKeyValues idValues) {
        return repository.existsById(idValues);
    }
}
