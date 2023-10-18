package com.unirutas.core.factory.connection.interfaces;

import com.unirutas.core.database.connection.interfaces.IConnectionPool;

public interface IConnectionPoolFactory {
    /**
     * Create a connection pool.
     * @return The connection pool.
     */
    IConnectionPool<?> createConnectionPool();
}
