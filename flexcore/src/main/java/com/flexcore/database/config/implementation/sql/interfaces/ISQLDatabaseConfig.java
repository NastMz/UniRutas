package com.flexcore.database.config.implementation.sql.interfaces;

import com.flexcore.database.config.interfaces.IDatabaseConfig;

/**
 * An interface for SQL database configurations.
 */
public interface ISQLDatabaseConfig extends IDatabaseConfig {
    /**
     * Get the JDBC driver for the configured SQL database.
     * @return The JDBC driver.
     */
    String getDriver();

    /**
     * Get the JDBC URL for the configured SQL database.
     * @return The JDBC URL.
     */
    String getUrl();
}
