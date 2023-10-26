package com.unirutas.core.factory.builder.interfaces;

import com.unirutas.core.builder.query.interfaces.ICustomQueryBuilder;

public interface ICustomQueryBuilderFactory {
    ICustomQueryBuilder createCustomQueryBuilder(Class<?> clazz);
}
