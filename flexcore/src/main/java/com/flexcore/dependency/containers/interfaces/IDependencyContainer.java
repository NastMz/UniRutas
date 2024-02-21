package com.flexcore.dependency.containers.interfaces;

public interface IDependencyContainer {
    Object getDependency(Class<?> clazz);
    void initializeSubDependency(Object clazz);
}
