package com.flexcore.dependency.utils;

import com.flexcore.dependency.annotations.*;
import com.flexcore.dependency.validators.DependencyValidator;
import com.flexcore.utils.ClasspathScanner;
import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.scanners.Scanners;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DependencyScanner {

    private static final Logger logger = LoggerFactory.getLogger(DependencyScanner.class);
    private static final List<String> packages = ClasspathScanner.getPackagesToScan();

    /**
     * Find all classes in the classpath that are annotated with {@link Inject}, {@link Repository}, {@link Factory} or {@link Singleton}.
     */
    public static List<Class<?>> scanClasses() {
        logger.info("Scanning classes for dependency injection.");

        List<Class<?>> classes = new ArrayList<>();

        packages.add("com.flexcore");

        Reflections reflections = new Reflections(packages.toArray(), new TypeElementsScanner());

        Collection<Map<String, Set<String>>> map = reflections.getStore().values();

        for (Map<String, Set<String>> stringSetMap : map) {
            for (String string : stringSetMap.keySet()) {
                    getClass(string, classes);
            }
        }

        logger.info("Classes scanned for dependency injection.");

        DependencyValidator.validate(classes);

        logger.debug("Classes found to inject: ");

        for (Class<?> clazz : classes) {
            logger.debug("\t" + clazz.getName());
        }

        return classes;
    }

    private static void getClass(String className, List<Class<?>> classes) {

        boolean isInPackage = false;

        // Check if the class is in one of the packages to scan. If not, return.
        for (String packageName : packages) {
            logger.debug("Checking if class " + className + " is in package " + packageName);
            if (className.startsWith(packageName)) {
                isInPackage = true;
                break;
            }
        }

        if (!isInPackage) {
            return;
        }

        logger.debug("Scanning class " + className + " for dependency injection.");

        try {
            Class<?> clazz = Class.forName(className);
            if (isValidClass(clazz) && !classes.contains(clazz)) {
                classes.add(clazz);
            } else {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Inject.class)) {
                        DependencyValidator.validateInject(field);

                        // Get the class of the field.
                        Class<?> fieldClass = field.getType();

                        Class<?> implementationClass;

                        String implementationClassName = field.getAnnotation(Inject.class).value();

                        if (!implementationClassName.isEmpty()) {
                            Class<?> injectClass = ClasspathScanner.getClass(implementationClassName);

                            if (injectClass == null) {
                                String message = "Error initializing inject instance of class " + fieldClass.getSimpleName() + ". Class " + implementationClassName + " not found.";
                                logger.error(message);
                                throw new RuntimeException(message);
                            } else {
                                implementationClass = injectClass;
                            }
                        } else {
                            implementationClass = fieldClass;
                        }

                        // Add the class to the list of classes.
                        if (!classes.contains(implementationClass)) {
                            classes.add(implementationClass);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            logger.warn("Class not found: " + className, e);
        }
    }

    /**
     * Check if the given class is valid for dependency injection.
     * @param clazz The class to check.
     * @return True if the class is valid, false otherwise.
     */
    private static boolean isValidClass(Class<?> clazz) {
        return clazz.isAnnotationPresent(Singleton.class) ||
                clazz.isAnnotationPresent(Factory.class) ||
                clazz.isAnnotationPresent(Implementation.class) ||
                clazz.isAnnotationPresent(Repository.class);
    }
}
