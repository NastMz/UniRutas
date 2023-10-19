package com.unirutas.core.dependency.injector.implementation;

import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.core.dependency.containers.implementations.DependencyContainer;
import com.unirutas.core.dependency.containers.interfaces.IDependencyContainer;
import com.unirutas.core.dependency.injector.interfaces.IDependencyInjector;
import com.unirutas.core.utils.ClasspathScanner;

import java.lang.reflect.Field;
import java.util.logging.Logger;

public class DependencyInjector implements IDependencyInjector {
    private static final IDependencyContainer dependencyContainer = DependencyContainer.getInstance();
    private static final Logger logger = Logger.getLogger(DependencyInjector.class.getName());

    public void injectDependencies(Object clazzToInject) {
        Field[] fields = clazzToInject.getClass().getDeclaredFields();

        for (Field field : fields){
            if (field.isAnnotationPresent(Inject.class)){
                String implementationClassName = field.getAnnotation(Inject.class).value();
                field.setAccessible(true);
                try {
                    if (!implementationClassName.isEmpty()){
                        Class<?> fieldClass = ClasspathScanner.getClass(implementationClassName);
                        Object dependencyClass = dependencyContainer.getDependency(fieldClass).getClass();
                        field.set(clazzToInject, dependencyClass);
                    } else {
                        Class<?> fieldClass = field.getType();
                        Object dependencyClass = dependencyContainer.getDependency(fieldClass);

                        field.set(clazzToInject, dependencyClass);
                    }
                } catch (IllegalAccessException e) {
                    String message = "Error injecting dependency in class" + clazzToInject.getClass().getSimpleName() + ". Field " + field.getName() + " is not accessible.";
                    logger.severe(message);
                    throw new RuntimeException(message, e);
                }
            }
        }
    }
}
