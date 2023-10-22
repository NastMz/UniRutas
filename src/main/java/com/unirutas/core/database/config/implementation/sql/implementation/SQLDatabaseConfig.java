package com.unirutas.core.database.config.implementation.sql.implementation;

import com.unirutas.core.database.config.implementation.sql.interfaces.ISQLDatabaseConfig;
import com.unirutas.core.database.enums.SQLDatabaseEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

public class SQLDatabaseConfig implements ISQLDatabaseConfig {
    private static SQLDatabaseConfig instance;
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    private String driver;
    private String url;

    private static final Logger logger = LoggerFactory.getLogger(SQLDatabaseConfig.class);

    private SQLDatabaseConfig() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            Properties clientConfig = new Properties();
            clientConfig.load(is);

            host = clientConfig.getProperty("db.host");
            port = clientConfig.getProperty("db.port");
            database = clientConfig.getProperty("db.database");
            user = clientConfig.getProperty("db.user");
            password = clientConfig.getProperty("db.password");
            String databaseEngine = clientConfig.getProperty("db.engine");

            loadInternalConfig(databaseEngine);
        } catch (IOException e) {
            handleException("Error loading client database configuration", e);
        }
    }

    private void loadInternalConfig(String databaseEngine) {
        try {
            SQLDatabaseEngine sqlDatabaseEngine = SQLDatabaseEngine.valueOf(databaseEngine.toUpperCase());

            driver = sqlDatabaseEngine.getDriver();
            String urlPattern = sqlDatabaseEngine.getUrlPattern();
            url = String.format(urlPattern, host, port, database);
        } catch (IllegalArgumentException e) {
            handleException("Error loading internal database configuration", e);
        }
    }

    /**
     * Get the instance of the DatabaseConfig.
     *
     * @return The DatabaseConfig instance.
     */
    public static synchronized SQLDatabaseConfig getInstance() {
        if (instance == null) {
            instance = new SQLDatabaseConfig();
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

    private void handleException(String message, Exception e) {
        logger.error(message);
        throw new RuntimeException(message, e);
    }
}
