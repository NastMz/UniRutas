package com.unirutas.core.dependency.containers.implementations;

import com.unirutas.core.dependency.annotations.*;
import com.unirutas.core.dependency.containers.interfaces.IDependencyContainer;
import com.unirutas.core.dependency.utils.DependencyScanner;
import com.unirutas.core.utils.ClasspathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyContainer implements IDependencyContainer {
    private final Map<Class<?>, Object> singletonInstances;
    private final Map<Class<?>, Object> factoryInstances;
    private final Map<Class<?>, Object> implementations;
    private final Map<Class<?>, Object> repositories;
    private final Map<Class<?>, Object> injects;
    private static DependencyContainer instance;
    private static final Logger logger = LoggerFactory.getLogger(DependencyContainer.class);

    private DependencyContainer() {
        singletonInstances = new HashMap<>();
        factoryInstances = new HashMap<>();
        implementations = new HashMap<>();
        repositories = new HashMap<>();
        injects = new HashMap<>();
        List<Class<?>> classes = DependencyScanner.scanClasses();
        initializeContainer(classes);
        for (Object clazz : injects.values()) {
            initializeSubDependency(clazz);
        }

        for (Object clazz : implementations.values()) {
            initializeSubDependency(clazz);
        }

        for (Object clazz : repositories.values()) {
            initializeSubDependency(clazz);
        }

        for (Object clazz : singletonInstances.values()) {
            initializeSubDependency(clazz);
        }

        for (Object clazz : factoryInstances.values()) {
            initializeSubDependency(clazz);
        }
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
            } else {
                try {
                    injects.put(clazz, clazz.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    String message = "Error initializing inject instance of class " + clazz.getName();
                    logger.error(message);
                    throw new RuntimeException(message, e);
                }
            }
        }
    }

    public void initializeSubDependency(Object clazz) {
        Field[] fields = clazz.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                String implementationClassName = field.getAnnotation(Inject.class).value();
                field.setAccessible(true);
                try {
                    if (!implementationClassName.isEmpty()) {
                        Class<?> fieldClass = ClasspathScanner.getClass(implementationClassName);
                        Object dependencyClass = getDependency(fieldClass);
                        field.set(clazz, dependencyClass);
                    } else {
                        Class<?> fieldClass = ClasspathScanner.getClass(field.getType().getName());
                        Object dependencyClass = getDependency(fieldClass);

                        field.set(clazz, dependencyClass);
                    }
                } catch (IllegalAccessException e) {
                    String message = "Error injecting dependency in class" + clazz.getClass().getSimpleName() + ". Field " + field.getName() + " is not accessible.";
                    logger.error(message);
                    throw new RuntimeException(message, e);
                }
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
            logger.error(message);
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
            logger.error(message);
            throw new RuntimeException(message, e);
        }
    }

    private void initializeRepository(Class<?> clazz) {
        try {
            repositories.put(clazz, clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            String message = "Error initializing repository instance of class " + clazz.getName();
            logger.error(message);
            throw new RuntimeException(message, e);
        }
    }

    private void initializeInject(Class<?> clazz) {
        try {
            injects.put(clazz, clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            String message = "Error initializing inject instance of class " + clazz.getName();
            logger.error(message);
            throw new RuntimeException(message, e);
        }
    }

    public void initializeImplementation(Class<?> clazz) {
        try {
            implementations.put(clazz, clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            String message = "Error initializing implementation instance of class " + clazz.getName();
            logger.error(message);
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
            logger.error(message);
            throw new RuntimeException(message);
        }
    }
}
