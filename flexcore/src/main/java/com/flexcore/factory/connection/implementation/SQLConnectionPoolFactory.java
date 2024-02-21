package com.flexcore.factory.connection.implementation;

import com.flexcore.database.connection.implementation.sql.implementation.SQLConnectionPool;
import com.flexcore.database.connection.interfaces.IConnectionPool;
import com.flexcore.factory.connection.interfaces.IConnectionPoolFactory;

import java.sql.Connection;

public class SQLConnectionPoolFactory implements IConnectionPoolFactory {
    @Override
    public IConnectionPool<Connection> createConnectionPool() {
        return SQLConnectionPool.getInstance();
    }
}