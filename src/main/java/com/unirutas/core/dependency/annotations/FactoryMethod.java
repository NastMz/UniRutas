package com.unirutas.core.dependency.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * FactoryMethod annotation. Used to mark a method as a factory method.
 * A factory method is a method that returns an object. Only one method can be annotated with @FactoryMethod in a class.
 * Also, the method must be static and have no parameters.
 * The class that contains the factory method must be annotated with @Factory.
 * @see Factory
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FactoryMethod {
}
