package com.flexcore.factory.config.interfaces;

import com.flexcore.database.config.interfaces.IDatabaseConfig;

public interface IDatabaseConfigFactory {
    /**
     * Create a database configuration.
     * @return The database configuration.
     */
    IDatabaseConfig createDatabaseConfig();
}