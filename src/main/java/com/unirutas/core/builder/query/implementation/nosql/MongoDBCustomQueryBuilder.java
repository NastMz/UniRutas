package com.unirutas.core.builder.query.implementation.nosql;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.unirutas.core.annotations.Table;
import com.unirutas.core.builder.query.interfaces.ICustomQueryBuilder;
import com.unirutas.core.builder.query.types.ComposedJoinInfo;
import com.unirutas.core.builder.query.types.JoinInfo;
import com.unirutas.core.builder.query.types.Tuple;
import com.unirutas.core.builder.query.utils.QueryBuilderUtils;
import com.unirutas.core.database.connection.implementation.nosql.implementation.MongoDBConnectionPool;
import com.unirutas.core.database.repository.utils.MongoDBRepositoryUtils;
import com.unirutas.core.providers.ConnectionPoolFactoryProvider;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MongoDBCustomQueryBuilder<T> implements ICustomQueryBuilder<T> {
    private final MongoDBConnectionPool connectionPool;
    private Bson filter = new Document();
    private final String collectionName;
    private final Class<T> clazz;
    private final Logger logger = LoggerFactory.getLogger(MongoDBCustomQueryBuilder.class);
    private final List<Class<?>> entities = new ArrayList<>();  // List of entities that have been joined
    private boolean hasJoined;
    private final List<String> fields = new ArrayList<>();// Flag to track if a join has been called
    private final List<Tuple<String, String>> joinFields = new ArrayList<>();
    private final List<JoinInfo> joinConditions = new ArrayList<>();
    private final List<ComposedJoinInfo> composedJoinConditions = new ArrayList<>();

    public MongoDBCustomQueryBuilder(Class<T> clazz) {
        this.connectionPool = (MongoDBConnectionPool) ConnectionPoolFactoryProvider.getFactory().createConnectionPool();
        this.collectionName = MongoDBRepositoryUtils.getCollectionName(clazz);
        this.clazz = clazz;
        entities.add(clazz);
    }

    public ICustomQueryBuilder<T> select() {
        return this;
    }

    public ICustomQueryBuilder<T> fields(String... fields) {
        for (String field : fields) {
            QueryBuilderUtils.checkField(field, clazz);
            this.fields.add(field);
        }
        return this;
    }

    public ICustomQueryBuilder<T> where(String field, Object value) {
        QueryBuilderUtils.checkField(field, clazz);
        filter = Filters.eq(field, value.toString());
        return this;
    }

    public ICustomQueryBuilder<T> and(String field, Object value) {
        QueryBuilderUtils.checkField(field, clazz);
        filter = Filters.and(filter, Filters.eq(field, value.toString()));
        return this;
    }

    public ICustomQueryBuilder<T> or(String field, Object value) {
        QueryBuilderUtils.checkField(field, clazz);
        filter = Filters.or(filter, Filters.eq(field, value.toString()));
        return this;
    }

    public ICustomQueryBuilder<T> join(String sourceField, Class<?> targetEntity, String targetField) {
        QueryBuilderUtils.checkField(sourceField, clazz);
        QueryBuilderUtils.checkTable(targetEntity, targetField);

        if (!entities.contains(targetEntity)) {
            entities.add(targetEntity);
        }

        hasJoined = true;

        joinConditions.add(new JoinInfo(targetEntity.getAnnotation(Table.class).name(), sourceField, targetField));

        return this;
    }

    public ICustomQueryBuilder<T> join(Class<?> sourceEntity, String sourceField, Class<?> targetEntity, String targetField) {
        if (!hasJoined) {
            String message = "You can't chain a new join before calling the initial join.";
            logger.error(message);
            throw new IllegalStateException(message);
        }

        QueryBuilderUtils.checkField(sourceField, sourceEntity);
        QueryBuilderUtils.checkField(targetField, targetEntity); // Ensure target field exists in the target collection

        if (!entities.contains(sourceEntity)) {
            String message = "You can't use the entity '" + sourceEntity.getSimpleName() + "' as the source table because it hasn't been joined.";
            logger.error(message);
            throw new IllegalStateException(message);
        }

        if (!entities.contains(targetEntity)) {
            entities.add(targetEntity);
        }

        composedJoinConditions.add(new ComposedJoinInfo(sourceEntity.getAnnotation(Table.class).name(), sourceField, targetEntity.getAnnotation(Table.class).name(), targetField));

        return this;
    }

    public ICustomQueryBuilder<T> joinFields(String... fields) {
        // Ensure that a join operation has been called before using joinFields
        if (!hasJoined) {
            throw new IllegalArgumentException("You can't add join fields before calling the initial join.");
        }

        for (String field : fields) {
            // Check if the field exists in any of the joined entities
            if (!QueryBuilderUtils.isFieldInEntities(field, entities)) {
                StringBuilder joinedEntities = new StringBuilder();
                for (Class<?> entity : entities) {
                    joinedEntities.append(entity.getSimpleName()).append(", ");
                }
                joinedEntities.delete(joinedEntities.length() - 2, joinedEntities.length() - 1);
                throw new IllegalArgumentException("The field '" + field + "' doesn't exist in any of the entities (" + joinedEntities + ") that have been joined.");
            }
        }

        for (String field : fields) {
            String targetTable = QueryBuilderUtils.getFieldTable(field, entities);
            joinFields.add(new Tuple<>(targetTable, field));
        }

        return this;
    }


    public List<List<Tuple<String, Object>>> execute() {
        try (MongoClient client = connectionPool.getConnection()) {
            MongoDatabase database = MongoDBRepositoryUtils.loadDatabase(client);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            List<List<Tuple<String, Object>>> results = new ArrayList<>();

            try {
                List<Document> documents = collection.find(filter).into(new ArrayList<>());

                List<String> joinFieldsList = new ArrayList<>();
                for (Tuple<String, String> joinField : this.joinFields) {
                    joinFieldsList.add(joinField.getValue());
                }

                // Create a temporary collection name
                String tempCollectionName = "temp_" + collectionName;

                // Create a temporary collection
                database.createCollection(tempCollectionName);

                for (Document document : documents) {
                    Set<String> documentKeys = new HashSet<>(document.keySet()); // Create a copy of keys to avoid concurrent modification

                    // Remove the field that is not in the joinFields list
                    for (String field : documentKeys) {
                        if (!fields.contains(field)) {
                            document.remove(field);
                        }
                    }

                    for (JoinInfo joinInfo : joinConditions) {

                        // Perform the inner join using the aggregation framework
                        Bson lookupStage = Aggregates.lookup(joinInfo.getTargetTable(), joinInfo.getSourceField(), joinInfo.getTargetField(), "joined");
                        Bson filterStage = Aggregates.match(filter);

                        List<Document> joinedDocuments = collection.aggregate(List.of(filterStage, lookupStage)).into(new ArrayList<>());

                        for (Document joinedDocument : joinedDocuments) {
                            // Remove the joined field from the joined document

                            List<Document> joined = (List<Document>) joinedDocument.get("joined");

                            for (Document joinedField : joined) {
                                Set<String> joinedFieldKeys = new HashSet<>(joinedField.keySet()); // Create a copy of keys to avoid concurrent modification

                                // Remove the field that is not in the joinFields list
                                for (String field : joinedFieldKeys) {
                                    if (!joinFieldsList.contains(field)) {
                                        joinedField.remove(field);
                                    }
                                }

                                // Insert the joined document into the temporary collection
                                Document tempDocument = new Document();
                                tempDocument.putAll(document);
                                tempDocument.putAll(joinedField);
                                database.getCollection(tempCollectionName).insertOne(tempDocument);
                            }
                        }
                    }

                    for (ComposedJoinInfo joinInfo : composedJoinConditions) {
                        // Perform the inner join using the aggregation framework
                        Bson lookupStage = Aggregates.lookup(joinInfo.getTargetTable(), joinInfo.getSourceField(), joinInfo.getTargetField(), "joined");

                        List<Document> joinedDocuments = database.getCollection(tempCollectionName).aggregate(List.of(lookupStage)).into(new ArrayList<>());

                        // Drop the temporary collection
                        database.getCollection(tempCollectionName).drop();

                        // Create a new temporary collection
                        database.createCollection(tempCollectionName);

                        for (Document joinedDocument : joinedDocuments) {
                            // Remove the joined field from the joined document
                            List<Document> joined = (List<Document>) joinedDocument.get("joined");

                            Document tempDocument = new Document();

                            tempDocument.putAll(joinedDocument);

                            for (Document joinedField : joined) {
                                Set<String> joinedFieldKeys = new HashSet<>(joinedField.keySet()); // Create a copy of keys to avoid concurrent modification

                                // Remove the field that is not in the joinFields list
                                for (String field : joinedFieldKeys) {
                                    if (!joinFieldsList.contains(field)) {
                                        joinedField.remove(field);
                                    }
                                }

                                tempDocument.putAll(joinedField);

                                // Insert the joined document into the temporary collection
                                database.getCollection(tempCollectionName).insertOne(tempDocument);
                            }
                        }
                        // Get the documents from the temporary collection
                        List<Document> tempDocuments = database.getCollection(tempCollectionName).find().into(new ArrayList<>());

                        // Drop the temporary collection
                        database.getCollection(tempCollectionName).drop();

                        for (Document tempDocument : tempDocuments) {
                            results.add(QueryBuilderUtils.mapDocumentToEntities(tempDocument, entities));
                        }

                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error executing query: \n\t\t" + e.getMessage());
            }

            return results;
        }
    }

}
