package com.unirutas.core.database.enums;

/**
 * This enumeration defines the SQL database engines supported by the framework. Each database engine is identified by
 * a unique engine and includes specific driver and URL pattern.
 * <p>
 * - The `engine` attribute is used for identifying the database engine.
 * <p>
 * - The `driver` attribute specifies the JDBC driver class name for the database engine.
 * <p>
 * - The `urlPattern` attribute indicates the URL pattern for the database engine.
 */
public enum SQLDatabaseEngine {
    POSTGRESQL("postgresql", "org.postgresql.Driver", "jdbc:postgresql://%s:%s/%s"),
    MYSQL("mysql", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://%s:%s/%s");

    private final String engine; // The database engine type (e.g. postgresql)
    private final String driver; // The JDBC driver class name for the database engine (e.g. org.postgresql.Driver)
    private final String urlPattern; // The URL pattern for the database engine (e.g. jdbc:postgresql://%s:%s/%s)

    SQLDatabaseEngine(String engine, String driver, String urlPattern) {
        this.engine = engine;
        this.driver = driver;
        this.urlPattern = urlPattern;
    }

    public String getEngine() {
        return engine;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrlPattern() {
        return urlPattern;
    }
}
