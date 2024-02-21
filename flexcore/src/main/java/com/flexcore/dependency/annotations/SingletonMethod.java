package com.flexcore.dependency.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SingletonMethod annotation. Used to mark a method as a singleton method.
 * A singleton method is a method that returns an object. Only one method can be annotated with @SingletonMethod in a class.
 * Also, the method must be static and have no parameters.
 * The class that contains the singleton method must be annotated with @Singleton.
 * @see Singleton
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SingletonMethod {
}
