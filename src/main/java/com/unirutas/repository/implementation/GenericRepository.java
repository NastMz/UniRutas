package com.unirutas.repository.implementation;

import com.unirutas.annotations.Column;
import com.unirutas.annotations.PrimaryKey;
import com.unirutas.annotations.Table;
import com.unirutas.repository.interfaces.IDatabaseManager;
import com.unirutas.repository.interfaces.IRepository;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class GenericRepository<T> implements IRepository<T, Object> {
    private final IDatabaseManager dbManager = DatabaseManager.getInstance();
    private final Class<T> clazz; // Clase de la entidad que se va a manejar en el repositorio (Student, Driver, etc.)
    private final String tableName; // Nombre de la tabla en la base de datos que se va a manejar en el repositorio
    private final Logger logger = Logger.getLogger(GenericRepository.class.getName()); // Logger para mostrar mensajes en consola

    public GenericRepository(Class<T> clazz) {
        this.clazz = clazz;
        this.tableName = getTableName(clazz);
    }

    private String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            return tableAnnotation.name();
        }
        return null;
    }

    private T mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        T entity = null;
        try {
            entity = clazz.getDeclaredConstructor().newInstance();

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    String columnName = columnAnnotation.name();
                    field.setAccessible(true);
                    field.set(entity, resultSet.getObject(columnName));
                }
            }
        } catch (ReflectiveOperationException e) {
            logger.severe("Error al mapear resultados a entidad: " + e.getMessage());
        }

        return entity;
    }

    private Object[] getPrimaryKeyValues(T entity) {
        PrimaryKey primaryKeyAnnotation = clazz.getAnnotation(PrimaryKey.class);

        if (primaryKeyAnnotation != null) {
            String[] idColumnNames = primaryKeyAnnotation.value();

            if (idColumnNames.length > 0) {
                List<Object> idValues = new ArrayList<>();

                for (String idColumnName : idColumnNames) {
                    Field idField = findFieldByColumnName(entity, idColumnName);

                    if (idField != null) {
                        idField.setAccessible(true);

                        try {
                            idValues.add(idField.get(entity));
                        } catch (IllegalAccessException e) {
                            logger.severe("Error al obtener valores de clave primaria: " + e.getMessage());
                        }
                    }
                }

                if (idValues.size() == idColumnNames.length) {
                    return idValues.toArray();
                }
            }
        }

        throw new IllegalArgumentException("La entidad no tiene una clave primaria definida.");
    }

    // Método para encontrar un campo por el nombre de la columna en la entidad
    private Field findFieldByColumnName(T entity, String columnName) {
        Class<?> currentClass = entity.getClass();

        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(columnName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }

        return null;
    }

    private boolean isPrimaryKeyField(Field field, PrimaryKey primaryKeyAnnotation) {
        for (String idColumnName : primaryKeyAnnotation.value()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                if (columnAnnotation.name().equals(idColumnName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkColumnAnnotations() {
        Field[] fields = clazz.getDeclaredFields();
        boolean hasColumnAnnotation = false;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                hasColumnAnnotation = true;
                break; // Si se encuentra una anotación @Column, no es necesario verificar más campos.
            }
        }

        if (!hasColumnAnnotation) {
            throw new IllegalArgumentException("La entidad no tiene columnas asociadas.");
        }
    }


    private void checkAnnotations() {
        if (!clazz.isAnnotationPresent(Table.class)){
            throw new IllegalArgumentException("La entidad no tiene una tabla asociada.");
        }

        if (!clazz.isAnnotationPresent(PrimaryKey.class)){
            throw new IllegalArgumentException("La entidad no tiene una clave primaria definida.");
        }

        checkColumnAnnotations();
    }

    public void save(T entity) {
        checkAnnotations();

        if (existsById(getPrimaryKeyValues(entity))) {
            logger.warning("Un registro con la misma clave primaria ya existe. No se puede guardar.");
            return;
        }

        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder insertSQL = new StringBuilder("INSERT INTO " + tableName + " (");
            StringBuilder values = new StringBuilder(") VALUES (");

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    String columnName = columnAnnotation.name();

                    insertSQL.append(columnName).append(", ");
                    values.append("?, ");
                }
            }

            insertSQL.delete(insertSQL.length() - 2, insertSQL.length());
            values.delete(values.length() - 2, values.length());

            insertSQL.append(values).append(")");
            String sql = insertSQL.toString();

            List<Object> valuesList = new ArrayList<>();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);
                    valuesList.add(field.get(entity));
                }
            }

            int rowsAffected = dbManager.executeUpdate(sql, valuesList.toArray());

            if (rowsAffected > 0) {
                dbManager.commitTransaction();
                logger.info(clazz.getSimpleName() + " guardado exitosamente.");
            } else {
                dbManager.rollbackTransaction();
                logger.warning("El guardado de " + clazz.getSimpleName() + " falló. El registro no se ha guardado.");
            }
        } catch (SQLException | IllegalAccessException e) {
            logger.severe("Error al guardar entidad: " + e.getMessage());
            try {
                dbManager.rollbackTransaction();
            } catch (SQLException ex) {
                logger.severe("Error al hacer rollback: " + ex.getMessage());
            }
        } finally {
            try {
                dbManager.disconnect();
            } catch (SQLException e) {
                logger.severe("Error al desconectar: " + e.getMessage());
            }
        }
    }

    public void delete(Object idValues) {

        checkAnnotations();

        if (!existsById(idValues)) {
            logger.warning("El " + clazz.getSimpleName() + " no existe.");
            return;
        }

        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder deleteSQL = new StringBuilder("DELETE FROM " + tableName + " WHERE ");

            PrimaryKey primaryKeyAnnotation = clazz.getAnnotation(PrimaryKey.class);
            if (primaryKeyAnnotation == null || primaryKeyAnnotation.value().length == 0) {
                throw new IllegalArgumentException("La entidad no tiene una clave primaria definida.");
            }

            String[] idColumnNames = primaryKeyAnnotation.value();
            for (int i = 0; i < idColumnNames.length; i++) {
                deleteSQL.append(idColumnNames[i]).append(" = ?");
                if (i < idColumnNames.length - 1) {
                    deleteSQL.append(" AND ");
                }
            }

            String sql = deleteSQL.toString();

            int rowsAffected = dbManager.executeUpdate(sql, idValues);

            if (rowsAffected > 0) {
                dbManager.commitTransaction();
                logger.info(clazz.getSimpleName() + " eliminado exitosamente.");
            } else {
                dbManager.rollbackTransaction();
                logger.warning("La eliminación de " + clazz.getSimpleName() + " falló. El registro no se ha eliminado o no existe.");
            }
        } catch (SQLException e) {
            logger.severe("Error al eliminar entidad: " + e.getMessage());
            try {
                dbManager.rollbackTransaction();
            } catch (SQLException ex) {
                logger.severe("Error al hacer rollback: " + ex.getMessage());
            }
        } finally {
            try {
                dbManager.disconnect();
            } catch (SQLException e) {
                logger.severe("Error al desconectar: " + e.getMessage());
            }
        }
    }

    public void update(T entity) {
        checkAnnotations();

        if (!existsById(getPrimaryKeyValues(entity))) {
            logger.warning("El registro no existe, no se puede actualizar.");
            return;
        }

        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder updateSQL = new StringBuilder("UPDATE " + tableName + " SET ");
            PrimaryKey primaryKeyAnnotation = clazz.getAnnotation(PrimaryKey.class);

            Field[] fields = clazz.getDeclaredFields();

            List<Object> valuesList = new ArrayList<>();
            Object[] primaryKeyValues = getPrimaryKeyValues(entity);

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    String columnName = columnAnnotation.name();

                    if (primaryKeyAnnotation != null && primaryKeyAnnotation.value().length > 0 && isPrimaryKeyField(field, primaryKeyAnnotation)) {
                        // Si el campo es parte de la clave primaria, omitirlo en la actualización
                        continue;
                    }

                    updateSQL.append(columnName).append(" = ?, ");
                    field.setAccessible(true);
                    valuesList.add(field.get(entity));
                }
            }

            updateSQL.delete(updateSQL.length() - 2, updateSQL.length());

            if (primaryKeyAnnotation != null && primaryKeyAnnotation.value().length > 0) {
                updateSQL.append(" WHERE ");
                for (String idColumnName : primaryKeyAnnotation.value()) {
                    updateSQL.append(idColumnName).append(" = ? AND ");
                }
                updateSQL.delete(updateSQL.length() - 5, updateSQL.length());
                valuesList.addAll(Arrays.asList(primaryKeyValues));
            }

            String sql = updateSQL.toString();

            int rowsAffected = dbManager.executeUpdate(sql, valuesList.toArray());

            if (rowsAffected > 0) {
                dbManager.commitTransaction();
                logger.info(clazz.getSimpleName() + " actualizado exitosamente.");
            } else {
                dbManager.rollbackTransaction();
                logger.warning("La actualización de " + clazz.getSimpleName() + " falló. El registro no se ha actualizado o no existe.");
            }
        } catch (SQLException | IllegalAccessException e) {
            logger.severe("Error al actualizar entidad: " + e.getMessage());
            try {
                dbManager.rollbackTransaction();
            } catch (SQLException ex) {
                logger.severe("Error al hacer rollback: " + ex.getMessage());
            }
        } finally {
            try {
                dbManager.disconnect();
            } catch (SQLException e) {
                logger.severe("Error al desconectar: " + e.getMessage());
            }
        }
    }



    public boolean existsById(Object... idValues) {

        checkAnnotations();

        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder selectSQL = new StringBuilder("SELECT COUNT(*) FROM " + tableName + " WHERE ");
            PrimaryKey primaryKeyAnnotation = clazz.getAnnotation(PrimaryKey.class);

            if (primaryKeyAnnotation == null || primaryKeyAnnotation.value().length == 0) {
                throw new IllegalArgumentException("La entidad no tiene una clave primaria definida.");
            }

            String[] idColumnNames = primaryKeyAnnotation.value();
            for (String idColumnName : idColumnNames) {
                selectSQL.append(idColumnName).append(" = ? AND ");
            }

            selectSQL.delete(selectSQL.length() - 5, selectSQL.length());

            String sql = selectSQL.toString();

            ResultSet resultSet = dbManager.executeQuery(sql, idValues);

            if (resultSet.next()) {
                dbManager.commitTransaction();
                int count = resultSet.getInt(1);
                return count > 0;
            } else {
                dbManager.rollbackTransaction();
            }
        } catch (SQLException e) {
            logger.severe("Error al verificar si existe entidad por ID: " + e.getMessage());
            try {
                dbManager.rollbackTransaction();
            } catch (SQLException ex) {
                logger.severe("Error al hacer rollback: " + ex.getMessage());
            }
        } finally {
            try {
                dbManager.disconnect();
            } catch (SQLException e) {
                logger.severe("Error al desconectar: " + e.getMessage());
            }
        }
        return false;
    }

    public T findById(Object idValues) {

        checkAnnotations();

        T entity = null;
        ResultSet resultSet = null; // ResultSet para cerrar en finally

        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder selectSQL = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
            PrimaryKey primaryKeyAnnotation = clazz.getAnnotation(PrimaryKey.class);

            if (primaryKeyAnnotation == null || primaryKeyAnnotation.value().length == 0) {
                throw new IllegalArgumentException("La entidad no tiene una clave primaria definida.");
            }

            String[] idColumnNames = primaryKeyAnnotation.value();
            for (int i = 0; i < idColumnNames.length; i++) {
                selectSQL.append(idColumnNames[i]).append(" = ?");
                if (i < idColumnNames.length - 1) {
                    selectSQL.append(" AND ");
                }
            }

            String sql = selectSQL.toString();

            resultSet = dbManager.executeQuery(sql, idValues);

            if (resultSet.next()) {
                entity = mapResultSetToEntity(resultSet);
            }

            dbManager.commitTransaction();
        } catch (SQLException e) {
            logger.severe("Error al buscar por ID: " + e.getMessage());
            try {
                dbManager.rollbackTransaction();
            } catch (SQLException ex) {
                logger.severe("Error al hacer rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                dbManager.disconnect();
            } catch (SQLException e) {
                logger.severe("Error al cerrar ResultSet o desconectar: " + e.getMessage());
            }
        }

        return entity;
    }

    public List<T> findAll() {

        checkAnnotations();

        List<T> entities = new ArrayList<>();
        ResultSet resultSet = null; // ResultSet para cerrar en finally

        try {
            dbManager.connect();
            dbManager.startTransaction();

            String sql = "SELECT * FROM " + tableName;

            resultSet = dbManager.executeQuery(sql);

            while (resultSet.next()) {
                T entity = mapResultSetToEntity(resultSet);
                entities.add(entity);
            }

            dbManager.commitTransaction();
        } catch (SQLException e) {
            logger.severe("Error al buscar todos los registros: " + e.getMessage());
            try {
                dbManager.rollbackTransaction();
            } catch (SQLException ex) {
                logger.severe("Error al hacer rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                dbManager.disconnect();
            } catch (SQLException e) {
                logger.severe("Error al cerrar ResultSet o desconectar: " + e.getMessage());
            }
        }

        return entities;
    }
}

