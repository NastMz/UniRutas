package com.flexcore.dependency.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Singleton annotation. This annotation is used to mark a class as a singleton. The class must have a method annotated with @SingletonMethod.
 * @see SingletonMethod
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Singleton {
}
