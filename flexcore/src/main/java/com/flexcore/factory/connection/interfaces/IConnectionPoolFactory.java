package com.flexcore.factory.connection.interfaces;

import com.flexcore.database.connection.interfaces.IConnectionPool;

public interface IConnectionPoolFactory {
    /**
     * Create a connection pool.
     * @return The connection pool.
     */
    IConnectionPool<?> createConnectionPool();
}
