package com.unirutas.core.database.connection.implementation.nosql.interfaces;

import com.mongodb.client.MongoClient;
import com.unirutas.core.database.connection.interfaces.IConnectionPool;

public interface IMongoDBConnectionPool extends IConnectionPool<MongoClient> {
}
