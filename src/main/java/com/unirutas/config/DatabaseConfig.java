package com.unirutas.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static DatabaseConfig instance;
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    private String databaseType;
    private String driver;
    private String url;

    private DatabaseConfig() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            Properties props = new Properties();
            props.load(is);

            host = props.getProperty("db.host");
            port = props.getProperty("db.port");
            database = props.getProperty("db.database");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
            databaseType = props.getProperty("db.type");

            driver = getDriverForDatabaseType(databaseType);
            url = getUrlForDatabaseType(databaseType);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDriverForDatabaseType(String type) {
        return switch (type) {
            case "postgresql" -> "org.postgresql.Driver";
            case "mysql" -> "com.mysql.cj.jdbc.Driver";
            default -> throw new IllegalArgumentException("Tipo de base de datos no válido: " + type);
        };
    }

    private String getUrlForDatabaseType(String type) {
        switch (type) {
            case "postgresql":
                return String.format("jdbc:postgresql://%s:%s/%s", host, port, database);
            case "mysql":
                return String.format("jdbc:mysql://%s:%s/%s", host, port, database);
            default:
                throw new IllegalArgumentException("Tipo de base de datos no válido: " + type);
        }
    }

    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public String getUrl() {
        return url;
    }
    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }
}

