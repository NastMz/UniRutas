package com.flexcore.database.connection.implementation.sql.interfaces;

import com.flexcore.database.connection.interfaces.IConnectionPool;

import java.sql.Connection;

public interface ISQLConnectionPool extends IConnectionPool<Connection> {
    /**
     * Release a database connection back to the pool.
     * @param connection The database connection to release.
     */
    void releaseConnection(Connection connection);
}
