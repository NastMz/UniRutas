package com.unirutas.core.database.config.implementation.nosql.implementation;

import com.unirutas.core.database.config.implementation.nosql.interfaces.IMongoDBDatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

public class MongoDBConfig implements IMongoDBDatabaseConfig {
    private static MongoDBConfig instance;
    private String database;
    private String user;
    private String password;
    private String connectionString;

    private static final Logger logger = LoggerFactory.getLogger(MongoDBConfig.class);

    private MongoDBConfig() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            Properties props = new Properties();
            props.load(is);

            String host = props.getProperty("db.host");
            int port = Integer.parseInt(props.getProperty("db.port"));
            database = props.getProperty("db.database");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");

            // Build the connection string specific to MongoDB
            connectionString = String.format("mongodb://%s:%s@%s:%d/?retryWrites=true&w=majority", user, password, host, port);

        } catch (IOException e) {
            handleException("Error loading MongoDB configuration", e);
        }
    }

    /**
     * Get the instance of the MongoDBConfig.
     * @return The MongoDBConfig instance.
     */
    public static synchronized MongoDBConfig getInstance() {
        if (instance == null) {
            instance = new MongoDBConfig();
        }
        return instance;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase(){
        return database;
    }

    private void handleException(String message, Exception e) {
        logger.error(message);
        throw new RuntimeException(message, e);
    }
}
