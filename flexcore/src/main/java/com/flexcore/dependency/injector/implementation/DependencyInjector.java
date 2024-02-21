package com.flexcore.dependency.injector.implementation;

import com.flexcore.dependency.containers.implementations.DependencyContainer;
import com.flexcore.dependency.containers.interfaces.IDependencyContainer;
import com.flexcore.dependency.injector.interfaces.IDependencyInjector;

public class DependencyInjector implements IDependencyInjector {
    private static final IDependencyContainer dependencyContainer = DependencyContainer.getInstance();

    public void injectDependencies(Object clazzToInject) {
        dependencyContainer.initializeSubDependency(clazzToInject);
    }
}
