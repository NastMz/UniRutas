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

    private DatabaseConfig() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            Properties props = new Properties();
            props.load(is);

            host = props.getProperty("db.host");
            port = props.getProperty("db.port");
            database = props.getProperty("db.database");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public String getUrl() {
        return String.format("jdbc:postgresql://%s:%s/%s", host, port, database);
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}

