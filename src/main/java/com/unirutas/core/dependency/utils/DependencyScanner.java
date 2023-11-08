package com.unirutas.core.dependency.utils;

import com.unirutas.core.dependency.annotations.*;
import com.unirutas.core.dependency.validators.DependencyValidator;
import com.unirutas.core.utils.ClasspathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
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

        try {
            File jarFile = new File(DependencyScanner.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (jarFile.isFile() && jarFile.getName().endsWith(".jar")) {
                // Scan classes in a jar file (this is the case when the application is packaged as a jar file).
                JarFile jar = new JarFile(jarFile);
                Enumeration<JarEntry> entries = jar.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class") ) {
                        String className = entry.getName().replace("/", ".").replace(".class", "");
                        getClass(className, classes);
                    }
                }

                jar.close();
            } else if (jarFile.isDirectory()) {
                // Scan classes in a directory (this is the case when the application is not packaged as a jar file).
                List<Class<?>> classNames = findClassesInDirectory(jarFile, "");

                for (Class<?> className : classNames) {
                    getClass(className.getName(), classes);
                }
            }
        } catch (URISyntaxException | IOException e) {
            logger.error("Error scanning classes for dependency injection. \n", e.getMessage());
            throw new RuntimeException(e);
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

        // Check if the class is in one of the packages to scan. If not, return.
        for (String packageName : packages) {
            logger.debug("Checking if class " + className + " is in package " + packageName);
            if (!className.startsWith(packageName)) {
                return;
            }
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
     * Find all classes in the classpath that are in the given package. Is able to search recursively through subpackages.
     * @param directory The directory to search for classes.
     * @param packageName The package name for which to search.
     * @return A list of all classes in the given package.
     */
    private static List<Class<?>> findClassesInDirectory(File directory, String packageName) {
        logger.debug("Scanning directory " + directory.getName() + " for classes.");
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
                        logger.warn("Class not found: " + className, e);
                    }
                }
            }
        }
        logger.debug("Classes found in directory " + directory.getName() + ": ");
        for (Class<?> clazz : classNames) {
            logger.debug("\t" + clazz.getName());
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
