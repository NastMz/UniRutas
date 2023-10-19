package com.unirutas.core.dependency.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Implementation annotation. Used to mark a class as an implementation of an interface.
 * Is used in conjunction with the @Inject annotation to inject an implementation of an interface.
 * @see Inject
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Implementation {
}
