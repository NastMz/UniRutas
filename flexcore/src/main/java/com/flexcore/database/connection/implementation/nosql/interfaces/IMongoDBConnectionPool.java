package com.flexcore.database.connection.implementation.nosql.interfaces;

import com.mongodb.client.MongoClient;
import com.flexcore.database.connection.interfaces.IConnectionPool;

public interface IMongoDBConnectionPool extends IConnectionPool<MongoClient> {
}
