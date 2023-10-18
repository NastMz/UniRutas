package com.unirutas.core.database.repository.interfaces;

import com.unirutas.core.database.repository.utils.PrimaryKeyValues;

import java.util.List;
import java.util.Map;

public interface IRepository<T> {
    /**
     * Print the database engine date.
     */
    void getDatabaseEngineDate();

    /**
     * Print the database engine hour.
     */
    void getDatabaseEngineHour();

    /**
     * Find an entity by its primary key.
     *
     * @param idValues The values of the primary key.
     * @return The entity if found, null otherwise.
     */
    T findById(PrimaryKeyValues idValues);

    /**
     * Find all entities.
     *
     * @return A list of all entities.
     */
    List<T> findAll();

    /**
     * Save an entity in the database.
     *
     * @param entity The entity to save.
     */
    void save(T entity);

    /**
     * Delete an entity from the database.
     *
     * @param idValues The values of the primary key of the entity to delete.
     */
    void delete(PrimaryKeyValues idValues);

    /**
     * Update an entity in the database.
     *
     * @param entity The entity to update.
     */
    void update(T entity);

    /**
     * Check if an entity exists in the database by its primary key.
     *
     * @param idValues The values of the primary key.
     * @return True if the entity exists, false otherwise.
     */
    boolean existsById(PrimaryKeyValues idValues);

    static PrimaryKeyValues createPrimaryKeyValues(Map<String, Object> values) {
        return new PrimaryKeyValues(values);
    }
}
