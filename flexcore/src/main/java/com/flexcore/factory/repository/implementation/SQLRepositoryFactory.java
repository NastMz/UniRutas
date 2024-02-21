package com.flexcore.factory.repository.implementation;

import com.flexcore.database.repository.implementation.sql.SQLGenericRepository;
import com.flexcore.database.repository.interfaces.IRepository;
import com.flexcore.factory.repository.interfaces.IRepositoryFactory;

public class SQLRepositoryFactory implements IRepositoryFactory {
    @Override
    public <T> IRepository<T> createRepository(Class<T> entityClass) {
        return new SQLGenericRepository<>(entityClass);
    }
}
