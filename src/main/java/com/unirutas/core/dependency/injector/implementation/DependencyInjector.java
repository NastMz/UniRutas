package com.unirutas.core.dependency.injector.implementation;

import com.unirutas.core.dependency.containers.implementations.DependencyContainer;
import com.unirutas.core.dependency.containers.interfaces.IDependencyContainer;
import com.unirutas.core.dependency.injector.interfaces.IDependencyInjector;

public class DependencyInjector implements IDependencyInjector {
    private static final IDependencyContainer dependencyContainer = DependencyContainer.getInstance();

    public void injectDependencies(Object clazzToInject) {
        dependencyContainer.initializeSubDependency(clazzToInject);
    }
}
