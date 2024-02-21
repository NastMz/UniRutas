package com.flexcore.factory.repository.interfaces;

import com.flexcore.database.repository.interfaces.IRepository;

public interface IRepositoryFactory {
    <T> IRepository<T> createRepository(Class<T> entityClass);
}
