package com.unirutas.core.database.connection.implementation.nosql.implementation;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.unirutas.core.database.config.implementation.nosql.interfaces.IMongoDBDatabaseConfig;
import com.unirutas.core.database.connection.implementation.nosql.interfaces.IMongoDBConnectionPool;
import com.unirutas.core.providers.DatabaseConfigFactoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDBConnectionPool implements IMongoDBConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(MongoDBConnectionPool.class);
    private static MongoDBConnectionPool instance;
    private MongoClient mongoClient;

    private MongoDBConnectionPool() {
        try {
            IMongoDBDatabaseConfig databaseConfig = (IMongoDBDatabaseConfig) DatabaseConfigFactoryProvider.getFactory().createDatabaseConfig();

            // Create a MongoClientURI from the database config
            ConnectionString uri = new ConnectionString(databaseConfig.getConnectionString());

            // Create a MongoClient using the URI, which manages the connection pool
            this.mongoClient = MongoClients.create(uri);
        } catch (Exception e) {
            handleException("Error initializing MongoDB connection pool", e);
        }
    }

    /**
     * Get the instance of the MongoDBConnectionPool.
     *
     * @return The MongoDBConnectionPool instance.
     */
    public static synchronized MongoDBConnectionPool getInstance() {
        if (instance == null) {
            instance = new MongoDBConnectionPool();
        }
        return instance;
    }

    @Override
    public MongoClient getConnection() {
        // You can obtain a connection to the database from the MongoClient
        return mongoClient;
    }

    @Override
    public void closeAllConnections() {
        // To close the connection pool, simply close the MongoClient
        mongoClient.close();
    }

    private void handleException(String message, Exception e) {
        logger.error(message);
        throw new RuntimeException(message, e);
    }
}
