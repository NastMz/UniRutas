package com.flexcore.database.config.implementation.nosql.interfaces;

import com.flexcore.database.config.interfaces.IDatabaseConfig;

/**
 * An interface for NoSQL database configurations.
 */
public interface IMongoDBDatabaseConfig extends IDatabaseConfig {
    /**
     * Get the connection string for the configured NoSQL database.
     * @return The NoSQL database connection string.
     */
    String getConnectionString();
    String getDatabase();
}