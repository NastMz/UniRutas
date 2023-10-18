package com.unirutas.core.factory.config.interfaces;

import com.unirutas.core.database.config.interfaces.IDatabaseConfig;

public interface IDatabaseConfigFactory {
    /**
     * Create a database configuration.
     * @return The database configuration.
     */
    IDatabaseConfig createDatabaseConfig();
}