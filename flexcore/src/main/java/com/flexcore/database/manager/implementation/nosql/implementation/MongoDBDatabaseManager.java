package com.flexcore.database.manager.implementation.nosql.implementation;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.flexcore.database.connection.interfaces.IConnectionPool;
import com.flexcore.database.manager.implementation.nosql.interfaces.IMongoDBDatabaseManager;
import com.flexcore.database.repository.utils.MongoDBRepositoryUtils;
import com.flexcore.providers.ConnectionPoolFactoryProvider;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MongoDBDatabaseManager implements IMongoDBDatabaseManager {

    private static MongoDBDatabaseManager instance;
    private final MongoDatabase database;
    private static final Logger logger = LoggerFactory.getLogger(MongoDBDatabaseManager.class.getName());

    private MongoDBDatabaseManager() {
        IConnectionPool<MongoClient> connectionPool = (IConnectionPool<MongoClient>) ConnectionPoolFactoryProvider.getFactory().createConnectionPool();
        MongoClient client = connectionPool.getConnection();
        database = MongoDBRepositoryUtils.loadDatabase(client);
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
            String date = resultDoc.get("lastModified").toString();

            // Remove the time from the date string using a regex to match the date, we now that mongo date format is Sat Oct 21,
            // so we find the first space and remove everything after it (including the space) also te year is at the end of the string

            String year = date.replaceAll(".*\\s(\\d+)$", "$1");
            date = date.replaceAll("^(\\w+\\s\\w+\\s\\w+).*", "$1");
            date = date + ", " + year;

            message = "Database Engine Date: " + date;
        } else {
            message = "Error obtaining date from database engine";
        }

        logger.info(message);

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

        String hour = serverStatus.get("localTime").toString();

        // Remove the date from the time string using a regex to match the date, we now that mongo date format is Sat Oct 21,
        // so we find the first space and remove everything after it (including the space) also te year is at the end of the string

        String date = hour.replaceAll("^(\\w+\\s\\w+\\s\\w+).*", "$1");

        int length = date.length();

        hour = hour.substring(length + 1);

        String year = hour.replaceAll(".*\\s(\\d+)$", "$1");

        hour = hour.substring(0, hour.length() - year.length() - 1);

        String message = "Database Engine Hour: " + hour;

        logger.info(message);

    }

    private void handleException(String message, Exception e) {
        logger.error(message, e);
    }
}
