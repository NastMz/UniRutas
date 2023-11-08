package com.unirutas.core.dependency.validators;

import com.unirutas.core.database.repository.CrudRepository;
import com.unirutas.core.dependency.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class DependencyValidator {
    private static final Logger logger = LoggerFactory.getLogger(DependencyValidator.class);

    public static void validate(List<Class<?>> classes) {
        logger.info("Validating classes for dependency injection.");
        for (Class<?> clazz : classes) {
            logger.debug("Validating class " + clazz.getName());
            validateRepository(clazz);
            validateFactory(clazz);
            validateSingleton(clazz);
            validateImplementation(clazz);
        }
    }

    public static void validateInject(Field field){
        if (field.isAnnotationPresent(Inject.class)) {
            if (field.getType().isPrimitive()) {
                String message = "The field " + field.getName() + " of the class " + field.getDeclaringClass().getName() + " annotated with @Inject is a primitive type. The field must be a class";
                logger.warn(message);
                throw new RuntimeException(message);
            }
        }
    }

    /**
     * Validate that a class annotated with @Repository extends CrudRepository.
     *
     * @param clazz The class to validate.
     * @throws RuntimeException If a class annotated with @Repository does not extend CrudRepository or if a class extends CrudRepository but is not annotated with @Repository.
     * @see CrudRepository
     * @see Repository
     */
    private static void validateRepository(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Repository.class)) {
            if (!CrudRepository.class.isAssignableFrom(clazz)) {
                String message = "The class " + clazz.getName() + " annotated with @Repository must extends CrudRepository";
                logger.warn(message);
                throw new RuntimeException(message);
            }
        } else if (CrudRepository.class.isAssignableFrom(clazz)) {
            String message = "The class " + clazz.getName() + " extends CrudRepository but is not annotated with @Repository";
            logger.warn(message);
            throw new RuntimeException(message);
        }
    }

    /**
     * Validate that a class annotated with @Factory has a field annotated with @FactoryMethod. Also validate that the factory method has no parameters and returns a class.
     *
     * @param clazz The class to validate.
     * @throws RuntimeException If a class annotated with @Factory does not have a field annotated with @FactoryMethod or if the factory method has parameters or returns a primitive type.
     * @see Factory
     */
    private static void validateFactory(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Factory.class)) {

            int count = 0;

            Method[] methods = clazz.getDeclaredMethods();

            for (Method method : methods) {

                if (method.isAnnotationPresent(FactoryMethod.class)) {
                    count++;
                }

                // Check if is possible to get an instance of the class using this factory method

                try {
                    method.setAccessible(true);

                    // Check if the factory method has no parameters
                    if (method.getParameterCount() > 0) {
                        String message = "The method " + method.getName() + " of the class " + clazz.getName() + " annotated with @FactoryMethod has parameters. The factory method must have no parameters";
                        logger.warn(message);
                        throw new RuntimeException(message);
                    }

                    // Check if the factory method is static
                    if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                        String message = "The method " + method.getName() + " of the class " + clazz.getName() + " annotated with @FactoryMethod is not static. The factory method must be static";
                        logger.warn(message);
                        throw new RuntimeException(message);
                    }

                    // Check if the factory method returns a class

                    Class<?> returnType = method.invoke(null).getClass();

                    // Check if the return type of the factory method is a class, because it can be a primitive type
                    if (returnType.isPrimitive()) {
                        String message = "The class " + clazz.getName() + " annotated with @Factory has a factory method that returns a primitive type. The factory method must return a class";
                        logger.warn(message);
                        throw new RuntimeException(message);
                    }

                } catch (SecurityException | IllegalAccessException | IllegalArgumentException |
                         InvocationTargetException e) {
                    String message = "Error validating the class " + clazz.getName() + " annotated with @Factory";
                    logger.warn(message);
                    throw new RuntimeException(message, e);
                }

                if (count > 1) {
                    String message = "The class " + clazz.getName() + " annotated with @Factory has more than one method annotated with @FactoryMethod";
                    logger.warn(message);
                    throw new RuntimeException(message);
                } else if (count == 0) {
                    String message = "The class " + clazz.getName() + " annotated with @Factory has no method annotated with @FactoryMethod";
                    logger.warn(message);
                    throw new RuntimeException(message);
                }
            }
        }
    }

    /**
     * Validate that a class annotated with @Singleton has a method annotated with @SingletonMethod. Also validate that the singleton method has no parameters and returns a class.
     * @param clazz The class to validate.
     * @throws RuntimeException If a class annotated with @Singleton does not have a method annotated with @SingletonMethod or if the singleton method has parameters or returns a primitive type.
     * @see Singleton
     */
    private static void validateSingleton(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Singleton.class)) {
            Method[] methods = clazz.getDeclaredMethods();

            int count = 0;

            for (Method method : methods) {
                if (method.isAnnotationPresent(SingletonMethod.class)) {
                    count++;
                }
                // Check if is possible to get an instance of the class using this factory method

                try {
                    method.setAccessible(true);

                    // Check if the factory method has no parameters
                    if (method.getParameterCount() > 0) {
                        String message = "The method " + method.getName() + " of the class " + clazz.getName() + " annotated with @SingletonMethod has parameters. The singleton method must have no parameters";
                        logger.warn(message);
                        throw new RuntimeException(message);
                    }

                    // Check if the factory method is static
                    if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                        String message = "The method " + method.getName() + " of the class " + clazz.getName() + " annotated with @SingletonMethod is not static. The singleton method must be static";
                        logger.warn(message);
                        throw new RuntimeException(message);
                    }

                    // Check if the factory method returns a class

                    Class<?> returnType = method.invoke(null).getClass();

                    // Check if the return type of the factory method is a class, because it can be a primitive type
                    if (returnType.isPrimitive()) {
                        String message = "The class " + clazz.getName() + " annotated with @Singleton has a singleton method that returns a primitive type. The singleton method must return a class";
                        logger.warn(message);
                        throw new RuntimeException(message);
                    }

                } catch (SecurityException | IllegalAccessException | IllegalArgumentException |
                         InvocationTargetException e) {
                    String message = "Error validating the class " + clazz.getName() + " annotated with @Singleton";
                    logger.warn(message);
                    throw new RuntimeException(message, e);
                }
            }

            if (count > 1) {
                String message = "The class " + clazz.getName() + " annotated with @Singleton has more than one method annotated with @SingletonMethod";
                logger.warn(message);
                throw new RuntimeException(message);
            } else if (count == 0) {
                String message = "The class " + clazz.getName() + " annotated with @Singleton has no method annotated with @SingletonMethod";
                logger.warn(message);
                throw new RuntimeException(message);
            }

        }
    }

    private static void validateImplementation(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Implementation.class)) {
            if (clazz.isInterface()) {
                String message = "The class " + clazz.getName() + " annotated with @Implementation is an interface. The class must be a class, not an interface";
                logger.warn(message);
                throw new RuntimeException(message);
            }

            // Check if the class is inherited from an interface
            Class<?>[] interfaces = clazz.getInterfaces();

            if (interfaces.length == 0) {
                String message = "The class " + clazz.getName() + " annotated with @Implementation does not implement any interface. The class must implement an interface";
                logger.warn(message);
                throw new RuntimeException(message);
            }

            // Check if the class implements only one interface
            if (interfaces.length > 1) {
                String message = "The class " + clazz.getName() + " annotated with @Implementation implements more than one interface. The class must implement only one interface";
                logger.warn(message);
                throw new RuntimeException(message);
            }
        }
    }

}

