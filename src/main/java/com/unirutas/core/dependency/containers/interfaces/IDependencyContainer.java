package com.unirutas.core.dependency.containers.interfaces;

public interface IDependencyContainer {
    Object getDependency(Class<?> clazz);
    void initializeSubDependency(Object clazz);
}
