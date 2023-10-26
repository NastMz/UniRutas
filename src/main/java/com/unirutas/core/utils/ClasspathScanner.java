package com.unirutas.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClasspathScanner {
    private static final Logger logger = LoggerFactory.getLogger(ClasspathScanner.class);
    private static final String classpath = System.getProperty("java.class.path");
    private static final String[] classpathEntries = classpath.split(System.getProperty("path.separator"));

    /**
     * Find all classes in the classpath.
     * @return A list of all classes in the classpath.
     */
    public static List<String> scanClasses() {
        List<String> classes = new ArrayList<>();
        for (String classpathEntry : classpathEntries) {
            File file = new File(classpathEntry);

            if (file.isDirectory()) {
                List<String> classNames = findClassesInDirectory(file, "");

                for (String className : classNames) {
                    if (!classes.contains(className)) {
                        classes.add(className);
                    }
                }
            }
        }
        return classes;
    }

    /**
     * Find a class in the classpath and return it as a Class object if found.
     *
     * @param targetClassName The name of the class to find.
     * @return The class object if found, null otherwise.
     */
    public static Class<?> getClass(String targetClassName) {
        for (String classpathEntry : classpathEntries) {
            File file = new File(classpathEntry);

            if (file.isDirectory()) {
                List<String> classNames = findClassesInDirectory(file, "");

                for (String className : classNames) {
                    if (className.endsWith(targetClassName)) {
                        try {
                            return Class.forName(className);
                        } catch (ClassNotFoundException e) {
                            // Log the exception with context.
                            logger.warn("Class not found: " + className, e);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Find all classes in the classpath that are in the given package. Is able to search recursively through subpackages.
     *
     * @param directory    The directory to search for classes.
     * @param packageName  The package name for which to search.
     * @return A list of all classes in the given package.
     */
    private static List<String> findClassesInDirectory(File directory, String packageName) {
        List<String> classNames = new ArrayList<>();

        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classNames.addAll(findClassesInDirectory(file, packageName + file.getName() + "."));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + file.getName().replace(".class", "");

                    classNames.add(className);
                }
            }
        }

        return classNames;
    }
}
