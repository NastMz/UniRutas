package com.unirutas.core.factory.repository.implementation;

import com.unirutas.core.database.repository.implementation.sql.SQLGenericRepository;
import com.unirutas.core.database.repository.interfaces.IRepository;
import com.unirutas.core.factory.repository.interfaces.IRepositoryFactory;

public class SQLRepositoryFactory implements IRepositoryFactory {
    @Override
    public <T> IRepository<T> createRepository(Class<T> entityClass) {
        return new SQLGenericRepository<>(entityClass);
    }
}
