package com.flexcore.database.repository.utils;

import java.util.Map;

public class PrimaryKeyValues {
    Map<String, Object> values;

    /**
     * Create a new instance of PrimaryKeyValues.
     * @param values The values of the primary key. The key is the name of the column and the value is the value of the column.
     *              For example, if the primary key is composed of the columns "code" and "name", the map should be
     *               Map.of("code", "123", "name", "John Doe").
     *               The order of the columns does not matter.
     */
    public PrimaryKeyValues(Map<String, Object> values) {
        this.values = values;
    }

    public Map<String, Object> getValues() {
        return values;
    }
}
