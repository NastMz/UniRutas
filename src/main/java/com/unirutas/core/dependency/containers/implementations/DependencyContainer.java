package com.unirutas.core.dependency.containers.implementations;

import com.unirutas.core.dependency.annotations.*;
import com.unirutas.core.dependency.containers.interfaces.IDependencyContainer;
import com.unirutas.core.dependency.utils.DependencyScanner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DependencyContainer implements IDependencyContainer {
    private Map<Class<?>, Object> singletonInstances;
    private Map<Class<?>, Object> factoryInstances;
    private Map<Class<?>, Object> implementations;
    private Map<Class<?>, Object> repositories;
    private Map<Class<?>, Object> injects;
    private static DependencyContainer instance;
    private static final Logger logger = Logger.getLogger(DependencyContainer.class.getName());

    private DependencyContainer() {
        singletonInstances = new HashMap<>();
        factoryInstances = new HashMap<>();
        implementations = new HashMap<>();
        repositories = new HashMap<>();
        injects = new HashMap<>();
        List<Class<?>> classes = DependencyScanner.scanClasses();
        initializeContainer(classes);
    }

    public static DependencyContainer getInstance() {
        if (instance == null) {
            instance = new DependencyContainer();
        }

        return instance;
    }

    private void initializeContainer(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Singleton.class)) {
                initializeSingleton(clazz);
            } else if (clazz.isAnnotationPresent(Factory.class)) {
                initializeFactory(clazz);
            } else if (clazz.isAnnotationPresent(Repository.class)) {
                initializeRepository(clazz);
            } else if (clazz.isAnnotationPresent(Inject.class)) {
                initializeInject(clazz);
            } else if (clazz.isAnnotationPresent(Implementation.class)) {
                initializeImplementation(clazz);
            }
        }
    }

    private void initializeSingleton(Class<?> clazz) {
        try {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(SingletonMethod.class)) {
                    singletonInstances.put(clazz, field.getType().getDeclaredMethod(field.getName()).invoke(null));
                    break;
                }
            }
        } catch (Exception e) {
            String message = "Error initializing singleton instance of class " + clazz.getName();
            logger.severe(message);
            throw new RuntimeException(message, e);
        }
    }

    private void initializeFactory(Class<?> clazz) {
        try {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(FactoryMethod.class)) {
                    factoryInstances.put(clazz, field.getType().getDeclaredMethod(field.getName()).invoke(null));
                    break;
                }
            }
        } catch (Exception e) {
            String message = "Error initializing factory instance of class " + clazz.getName();
            logger.severe(message);
            throw new RuntimeException(message, e);
        }
    }

    private void initializeRepository(Class<?> clazz) {
        try {
            repositories.put(clazz, clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            String message = "Error initializing repository instance of class " + clazz.getName();
            logger.severe(message);
            throw new RuntimeException(message, e);
        }
    }

    private void initializeInject(Class<?> clazz) {
        try {
            injects.put(clazz, clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            String message = "Error initializing inject instance of class " + clazz.getName();
            logger.severe(message);
            throw new RuntimeException(message, e);
        }
    }

    public void initializeImplementation(Class<?> clazz) {
        try {
            implementations.put(clazz, clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            String message = "Error initializing implementation instance of class " + clazz.getName();
            logger.severe(message);
            throw new RuntimeException(message, e);
        }
    }

    public Object getDependency(Class<?> clazz) {
        if (singletonInstances.containsKey(clazz)) {
            return singletonInstances.get(clazz);
        } else if (factoryInstances.containsKey(clazz)) {
            return factoryInstances.get(clazz);
        } else if (repositories.containsKey(clazz)) {
            return repositories.get(clazz);
        } else if (injects.containsKey(clazz)) {
            return injects.get(clazz);
        } else if (implementations.containsKey(clazz)) {
            return implementations.get(clazz);
        } else {
            String message = "Error getting dependency of class " + clazz.getName() + ". Dependency not found.";
            logger.severe(message);
            throw new RuntimeException(message);
        }
    }
}
