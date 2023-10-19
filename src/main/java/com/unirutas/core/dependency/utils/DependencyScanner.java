package com.unirutas.core.dependency.utils;

import com.unirutas.core.dependency.annotations.*;
import com.unirutas.core.dependency.validators.DependencyValidator;
import com.unirutas.core.utils.ClasspathScanner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DependencyScanner {

    private static final Logger logger = Logger.getLogger(DependencyScanner.class.getName());
    private static final String classpath = System.getProperty("java.class.path");
    private static final String[] classpathEntries = classpath.split(System.getProperty("path.separator"));

    /**
     * Find all classes in the classpath that are annotated with {@link Inject}, {@link Repository}, {@link Factory} or {@link Singleton}.
     */
    public static List<Class<?>> scanClasses() {
        List<Class<?>> classes = new ArrayList<>();
        for (String classpathEntry : classpathEntries) {
            File file = new File(classpathEntry);

            if (file.isDirectory()) {
                List<Class<?>> classNames = findClassesInDirectory(file, "");

                for (Class<?> className : classNames) {
                    if (isValidClass(className) && !classes.contains(className)) {
                        classes.add(className);
                    } else {
                        Field[] fields = className.getDeclaredFields();
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
                                        logger.severe(message);
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
                }
            }
        }

        DependencyValidator.validate(classes);

        return classes;
    }

    /**
     * Find all classes in the classpath that are in the given package. Is able to search recursively through subpackages.
     * @param directory The directory to search for classes.
     * @param packageName The package name for which to search.
     * @return A list of all classes in the given package.
     */
    private static List<Class<?>> findClassesInDirectory(File directory, String packageName) {
        List<Class<?>> classNames = new ArrayList<>();

        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classNames.addAll(findClassesInDirectory(file, packageName + file.getName() + "."));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + file.getName().replace(".class", "");
                    try {
                        classNames.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        // Log the exception with context.
                        logger.log(Level.WARNING, "Class not found: " + className, e);
                    }
                }
            }
        }
        return classNames;
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
