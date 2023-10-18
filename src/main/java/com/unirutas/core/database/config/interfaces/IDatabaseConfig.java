package com.unirutas.core.database.config.interfaces;

/**
 * A common interface for all database configurations.
 */
public interface IDatabaseConfig {
    /**
     * Get the database user.
     * @return The database user.
     */
    String getUser();

    /**
     * Get the database password.
     * @return The database password.
     */
    String getPassword();
}