package com.flexcore.factory.builder.interfaces;

import com.flexcore.builder.query.interfaces.ICustomQueryBuilder;

public interface ICustomQueryBuilderFactory {
    ICustomQueryBuilder createCustomQueryBuilder(Class<?> clazz);
}
