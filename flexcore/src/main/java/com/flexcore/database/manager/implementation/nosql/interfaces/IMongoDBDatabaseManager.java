package com.flexcore.database.manager.implementation.nosql.interfaces;

import com.flexcore.database.manager.interfaces.IDatabaseManager;
import org.bson.Document;

import java.util.List;

public interface IMongoDBDatabaseManager extends IDatabaseManager {
    void insert(String collection, Document object);
    void update(String collectionName, Document query, Document update);
    void delete(String collection, Document object);
    Document find(String collection, Document object);
    List<Document> findAll(String collection);
}
