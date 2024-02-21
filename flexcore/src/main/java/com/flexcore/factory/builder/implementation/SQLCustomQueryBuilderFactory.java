package com.flexcore.factory.builder.implementation;

import com.flexcore.builder.query.implementation.sql.SQLCustomQueryBuilder;
import com.flexcore.builder.query.interfaces.ICustomQueryBuilder;
import com.flexcore.factory.builder.interfaces.ICustomQueryBuilderFactory;

public class SQLCustomQueryBuilderFactory implements ICustomQueryBuilderFactory {
    public ICustomQueryBuilder createCustomQueryBuilder(Class<?> clazz) {
        return new SQLCustomQueryBuilder(clazz);
    }
}
