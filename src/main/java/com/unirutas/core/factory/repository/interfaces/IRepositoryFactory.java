package com.unirutas.core.factory.repository.interfaces;

import com.unirutas.core.database.repository.interfaces.IRepository;

public interface IRepositoryFactory {
    <T> IRepository<T> createRepository(Class<T> entityClass);
}
