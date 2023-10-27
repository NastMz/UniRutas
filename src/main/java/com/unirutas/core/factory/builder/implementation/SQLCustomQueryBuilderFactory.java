package com.unirutas.core.factory.builder.implementation;

import com.unirutas.core.builder.query.implementation.sql.SQLCustomQueryBuilder;
import com.unirutas.core.builder.query.interfaces.ICustomQueryBuilder;
import com.unirutas.core.factory.builder.interfaces.ICustomQueryBuilderFactory;

public class SQLCustomQueryBuilderFactory implements ICustomQueryBuilderFactory {
    public ICustomQueryBuilder createCustomQueryBuilder(Class<?> clazz) {
        return new SQLCustomQueryBuilder(clazz);
    }
}
