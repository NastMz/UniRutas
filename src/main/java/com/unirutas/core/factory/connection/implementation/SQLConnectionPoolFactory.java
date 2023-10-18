package com.unirutas.core.factory.connection.implementation;

import com.unirutas.core.database.connection.implementation.sql.implementation.SQLConnectionPool;
import com.unirutas.core.database.connection.interfaces.IConnectionPool;
import com.unirutas.core.factory.connection.interfaces.IConnectionPoolFactory;

import java.sql.Connection;

public class SQLConnectionPoolFactory implements IConnectionPoolFactory {
    @Override
    public IConnectionPool<Connection> createConnectionPool() {
        return SQLConnectionPool.getInstance();
    }
}