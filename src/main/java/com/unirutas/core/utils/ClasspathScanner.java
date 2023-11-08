package com.unirutas.core.utils;

import com.unirutas.core.dependency.utils.DependencyScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ClasspathScanner {
    private static final Logger logger = LoggerFactory.getLogger(ClasspathScanner.class);
    private static final List<String> packages = getPackagesToScan();

    /**
     * Find a class in the classpath and return it as a Class object if found.
     *
     * @param targetClassName The name of the class to find.
     * @return The class object if found, null otherwise.
     */
    public static Class<?> getClass(String targetClassName) {

        for (String packageName : packages) {
            logger.debug("Checking if class " + targetClassName + " is in package " + packageName);
            if (!targetClassName.startsWith(packageName)) {
                return null;
            }
        }

        // Check if the class is in the classpath.
        try {
            Class<?> clazz = Class.forName(targetClassName);
            logger.debug("Class " + targetClassName + " found in classpath.");
            return clazz;
        } catch (ClassNotFoundException e) {
            logger.debug("Class " + targetClassName + " not found in classpath.");
            return null;
        }
    }

    /**
     * Find all classes in the classpath that are in the given package. Is able to search recursively through subpackages.
     *
     * @param directory   The directory to search for classes.
     * @param packageName The package name for which to search.
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

    public static List<String> getPackagesToScan() {
        logger.debug("Loading core configuration file.");
        try {
            // Load the xml file from the resources folder. This file contains the packages to scan for dependency injection.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream inputStream = DependencyScanner.class.getClassLoader().getResourceAsStream("core-config.xml");

            Document document = builder.parse(inputStream);

            // Get the packages to scan from the xml file.
            NodeList packageNodes = document.getElementsByTagName("package");

            List<String> packages = new ArrayList<>();

            for (int i = 0; i < packageNodes.getLength(); i++) {
                Node node = packageNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    packages.add(element.getTextContent());
                }
            }

            logger.debug("Packages to scan loaded from core configuration file.");

            return packages;

        } catch (Exception e) {
            logger.error("Error loading core configuration file. \n", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
