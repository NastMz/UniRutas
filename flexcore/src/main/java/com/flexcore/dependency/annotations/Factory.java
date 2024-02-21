package com.flexcore.dependency.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Factory annotation. Used to mark a class as a factory. The class must have a method annotated with @FactoryMethod.
 * @see FactoryMethod
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Factory {
}