package com.flexcore.database.connection.interfaces;

/**
 * A class to manage a connection pool for the database.
 */
public interface IConnectionPool<T> {
    /**
     * Get a database connection from the pool.
     * @return A database connection.
     */
    T getConnection();

    /**
     * Close all database connections in the pool.
     */
    void closeAllConnections();
}
