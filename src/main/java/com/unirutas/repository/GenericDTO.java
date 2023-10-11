package com.unirutas.repository;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenericDTO<T> {
    private final DatabaseManager dbManager = DatabaseManager.getInstance();
    private final Class<T> clazz;
    private final String tableName;

    public GenericDTO(Class<T> clazz, String tableName) {
        this.clazz = clazz;
        this.tableName = tableName;
    }

    public void insert(T entity) {
        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder insertSQL = new StringBuilder("INSERT INTO " + tableName + " (");
            StringBuilder values = new StringBuilder(") VALUES (");
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                insertSQL.append(field.getName()).append(", ");
                values.append("?, ");
            }

            insertSQL.delete(insertSQL.length() - 2, insertSQL.length()); // Elimina la última coma y espacio
            values.delete(values.length() - 2, values.length()); // Elimina la última coma y espacio

            insertSQL.append(values).append(")");
            String sql = insertSQL.toString();

            List<Object> valuesList = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                valuesList.add(field.get(entity));
            }

            int rowsAffected = dbManager.executeUpdate(sql, valuesList.toArray());

            if (rowsAffected > 0) {
                dbManager.commitTransaction();
                System.out.println(clazz.getSimpleName() + " creado exitosamente.");
            } else {
                dbManager.rollbackTransaction();
                System.out.println("La creación de " + clazz.getSimpleName() + " falló.");
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            try {
                dbManager.rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                dbManager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void delete(T entity) {
        try {
            dbManager.connect();
            dbManager.startTransaction();

            String deleteSQL = "DELETE FROM " + tableName + " WHERE code = ?";
            int rowsAffected = dbManager.executeUpdate(deleteSQL, entity);

            if (rowsAffected > 0) {
                dbManager.commitTransaction();
                System.out.println(clazz.getSimpleName() + " eliminado exitosamente.");
            } else {
                dbManager.rollbackTransaction();
                System.out.println("La eliminación de " + clazz.getSimpleName() + " falló. El usuario no existe.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                dbManager.rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                dbManager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(T entity) {
        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder updateSQL = new StringBuilder("UPDATE " + tableName + " SET ");
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                updateSQL.append(field.getName()).append(" = ?, ");
            }

            updateSQL.delete(updateSQL.length() - 2, updateSQL.length()); // Elimina la última coma y espacio
            updateSQL.append(" WHERE code = ?");
            String sql = updateSQL.toString();

            List<Object> valuesList = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                valuesList.add(field.get(entity));
            }

            int rowsAffected = dbManager.executeUpdate(sql, valuesList.toArray());

            if (rowsAffected > 0) {
                dbManager.commitTransaction();
                System.out.println(clazz.getSimpleName() + " actualizado exitosamente.");
            } else {
                dbManager.rollbackTransaction();
                System.out.println("La actualización de " + clazz.getSimpleName() + " falló. El usuario no existe.");
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            try {
                dbManager.rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                dbManager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: Agrega métodos para buscar, etc.
}
