package com.flexcore.builder.query.interfaces;

import com.flexcore.builder.query.types.Tuple;

import java.util.List;

/**
 * Custom query builder interface. This interface can be used to create custom queries. It is used to create a custom query of a specific entity.
 */
public interface ICustomQueryBuilder {

    /**
     * Starts the query builder. Equivalent to SELECT * FROM table;
     * @return The query builder.
     */
    ICustomQueryBuilder select();

    /**
     * Selects a field in the query. Equivalent to SELECT field FROM table;
     * Do not use this method if you want to select all fields.
     * @param fields The fields to select.
     * @return The query builder.
     */
    ICustomQueryBuilder fields(String... fields);

    /**
     * Adds a WHERE clause to the query. Equivalent to SELECT field FROM table WHERE field = value;
     * @param field The field to select.
     * @param value The value of the field.
     * @return The query builder.
     */
    ICustomQueryBuilder where(String field, Object value);

    /**
     * Adds an AND clause to the query. Equivalent to SELECT field FROM table WHERE field = value AND field2 = value2;
     * @param field The field to select.
     * @param value The value of the field.
     * @return The query builder.
     */
    ICustomQueryBuilder and(String field, Object value);

    /**
     * Adds an OR clause to the query. Equivalent to SELECT field FROM table WHERE field = value OR field2 = value2;
     * @param field The field to select.
     * @param value The value of the field.
     * @return The query builder.
     */
    ICustomQueryBuilder or(String field, Object value);

    /**
     * Executes the query created by the query builder.
     * The query builder will return a matrix of tuples. Each tuple will contain the field name and the value of the field.
     * The first list of tuples will contain the fields of the first record found and so on.
     * @return A matrix of tuples.
     */
    List<List<Tuple<String, Object>>> execute();

    /**
     * Adds a join clause to the query. Equivalent to SELECT field FROM table INNER JOIN targetTable ON table.field1 = targetTable.field2;
     * Use this method when the source table is the same as the entity table of the query builder param.
     * @see ICustomQueryBuilder#join(Class, String, Class, String)
     * @param sourceField The field of the source table.
     * @param targetEntity The target entity.
     * @param targetField The field of the target table.
     * @return The query builder.
     */
    ICustomQueryBuilder join(String sourceField, Class<?> targetEntity, String targetField);

    /**
     * Adds an additional join clause to the query, allowing for nested joins.
     * This method is equivalent to writing a SQL statement like:
     * <p>
     * SELECT field FROM table
     * <p>
     * INNER JOIN targetTable ON table.field1 = targetTable.field2
     * <p>
     * INNER JOIN sourceTable ON sourceTable.field3 = targetTable.field4;
     * <p>
     * Use this method when you need to join multiple tables in a query and nest those joins.
     * </p>
     * <p>
     * Before using this method, you must call the 'join' method with the initial join clause.
     * Furthermore, the source entity specified here must be the same as the target entity used in the previous 'join'.
     * In other words, you can't join a table with another table that is unrelated to the entity in the first join clause.
     * This restriction exists because the query builder relies on the entity from the first join to perform subsequent joins.
     * </p>
     * <p>
     * Attempting to use the 'join' method with a different entity table may cause the query to fail, and the result cannot be predicted.
     * </p>
     * @see ICustomQueryBuilder#join(String, Class, String)
     *
     * @param sourceEntity The source entity for the new join. (Must be the same as the target entity used in the previous 'join' method call).
     * @param sourceField The field in the source table for the new join.
     * @param targetEntity The target entity for the new join.
     * @param targetField The field in the target table for the new join.
     * @return The query builder instance for method chaining.
     */

    ICustomQueryBuilder join(Class<?> sourceEntity, String sourceField, Class<?> targetEntity, String targetField);

    /**
     * Specifies the fields to select in a query that includes join clauses. This is equivalent to the SQL statement:
     * SELECT table.field1, table.field2, targetTable.field3 FROM table INNER JOIN targetTable ON table.field1 = targetTable.field2;
     * <p>
     * To use this method, you must first call the 'join' method to establish the necessary join clauses.
     * </p>
     * <p>
     * It's important to provide only unique fields in the 'fields' parameter. If duplicate fields are passed, the query will fail.
     * In such cases, the query will use the first occurrence of the duplicated field, making the result unpredictable.
     * For example, if you have a 'name' field in both the 'table' and 'targetTable' tables, and you pass 'name' as a field to select,
     * the query will fail because the 'name' field is ambiguous. So avoid use the same field name in different tables.
     * </p>
     * <p>
     * Do not use this method if you want to select all fields.
     * @see ICustomQueryBuilder#join(String, Class, String)
     * @see ICustomQueryBuilder#join(Class, String, Class, String)
     *
     * @param fields The fields to select in the query. Must be unique. (e.g. field1, field2, field3). Do not use the same field name in different tables.
     * @return The query builder instance for further construction of the query.
     */

    ICustomQueryBuilder joinFields(String... fields);
}
