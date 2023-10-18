package com.unirutas.core.database.enums;

/**
 * This enumeration defines the NoSQL database engines supported by the framework. Each database engine is identified by
 * a unique engine and includes specific configuration, connection pool, database manager, and repository classes.
 * <p>
 * - The `engine` attribute is used for identifying the database engine.
 * <p>
 * - The `configuration class name` specifies the class used for loading the database engine's configuration.
 * <p>
 * - The `connection pool class name` indicates the class responsible for creating the connection pool for the engine.
 * <p>
 * - The `database manager class name` is used to create the database manager for the engine.
 * <p>
 * - The `repository class name` is used to create the repository for the engine.
 */

public enum NoSQLDatabaseEngine {
    MONGODB("mongodb", "MongoDBConfig", "MongoDBConnectionPool", "MongoDBDatabaseManager", "MongoDBGenericRepository");

    private final String engine; // The database engine type (e.g. mongodb)
    private final String config; // The configuration class name for the database engine (e.g. MongoDBConfig)
    private final String connection; // The connection pool class name for the database engine (e.g. MongoDBConnectionPool)
    private final String manager;  // The database manager class name for the database engine (e.g. MongoDBDatabaseManager)
    private final String repository; // The repository class name for the database engine (e.g. MongoDBGenericRepository)

    NoSQLDatabaseEngine(String engine, String config, String connection, String manager, String repository) {
        this.engine = engine;
        this.config = config;
        this.connection = connection;
        this.manager = manager;
        this.repository = repository;
    }

    public String getEngine() {
        return engine;
    }

    public String getConfig() {
        return config;
    }

    public String getConnection() {
        return connection;
    }

    public String getManager() {
        return manager;
    }

    public String getRepository() {
        return repository;
    }
}
