package com.unirutas.core.database.manager.implementation.nosql.implementation;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.unirutas.core.database.connection.interfaces.IConnectionPool;
import com.unirutas.core.database.manager.implementation.nosql.interfaces.IMongoDBDatabaseManager;
import com.unirutas.core.providers.ConnectionPoolFactoryProvider;
import org.bson.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDBDatabaseManager implements IMongoDBDatabaseManager {

    private static MongoDBDatabaseManager instance;
    private MongoClient client;
    private MongoDatabase database;
    private static Logger logger = Logger.getLogger(MongoDBDatabaseManager.class.getName());

    private MongoDBDatabaseManager() {
        IConnectionPool<MongoClient> connectionPool = (IConnectionPool<MongoClient>) ConnectionPoolFactoryProvider.getFactory().createConnectionPool();
        client = connectionPool.getConnection();
        loadDatabase();
    }

    private void loadDatabase() {
        try (InputStream is = ConnectionPoolFactoryProvider.class.getClassLoader().getResourceAsStream("database.properties")) {
            Properties props = new Properties();
            props.load(is);
            String dbName = props.getProperty("db.database");
            database = client.getDatabase(dbName);
        } catch (IOException e) {
            handleException("Error reading database engine from database.properties", e);
        }
    }

    public static synchronized MongoDBDatabaseManager getInstance() {
        if (instance == null) {
            instance = new MongoDBDatabaseManager();
        }
        return instance;
    }

    @Override
    public void insert(String collection, Document object) {
        try {
            database.getCollection(collection).insertOne(object);
        } catch (Exception e) {
            handleException("Error inserting document into collection: " + collection, e);
        }
    }

    @Override
    public void update(String collectionName, Document query, Document update) {
        try {
            database.getCollection(collectionName).updateMany(query, new Document("$set", update));
        } catch (Exception e) {
            handleException("Error updating documents in collection: " + collectionName, e);
        }
    }

    @Override
    public void delete(String collection, Document object) {
        try {
            database.getCollection(collection).deleteMany(object);
        } catch (Exception e) {
            handleException("Error deleting documents from collection: " + collection, e);
        }
    }

    @Override
    public Document find(String collection, Document object) {
        try {
            return database.getCollection(collection).find(object).first();
        } catch (Exception e) {
            handleException("Error finding document in collection: " + collection, e);
        }
        return null;
    }

    @Override
    public List<Document> findAll(String collectionName) {
        try {
            FindIterable<Document> documents = database.getCollection(collectionName).find();
            List<Document> resultList = new ArrayList<>();
            for (Document document : documents) {
                resultList.add(document);
            }
            return resultList;
        } catch (Exception e) {
            handleException("Error finding all documents in collection: " + collectionName, e);
        }
        return new ArrayList<>();
    }

    @Override
    public void connect() {
        throw new UnsupportedOperationException("This method is not supported for MongoDB");
    }

    @Override
    public void disconnect() {
        throw new UnsupportedOperationException("This method is not supported for MongoDB");
    }

    @Override
    public void getDate() {
        MongoCollection<Document> tempCollection = database.getCollection("temp");

        // Insert a dummy document
        Document tempDoc = new Document("temp", "temp");
        tempCollection.insertOne(tempDoc);

        // Obtaining the date from MongoDB engine
        Document updateDoc = new Document("$currentDate", new Document("lastModified", true));
        tempCollection.updateOne(tempDoc, updateDoc);
        Document resultDoc = tempCollection.find(tempDoc).first();

        String message;
        if (resultDoc != null) {
            message = "Database Engine Date: " + resultDoc.get("lastModified");
            logger.log(Level.INFO, message);
        } else {
            message = "Error obtaining date from database engine";
            logger.log(Level.SEVERE, message);
        }

        // Delete the dummy document
        tempCollection.deleteOne(tempDoc);

        // Drop the temp collection
        database.getCollection("temp").drop();
    }

    @Override
    public void getHour() {
        // Obtaining the hour from MongoDB engine
        Document command = new Document("serverStatus", 1);
        Document serverStatus = database.runCommand(command);

        String message = "Database Engine Hour: " + serverStatus.get("localTime");

        logger.log(Level.INFO, message);

    }

    private void handleException(String message, Exception e) {
        logger.log(Level.SEVERE, message, e);
    }
}
