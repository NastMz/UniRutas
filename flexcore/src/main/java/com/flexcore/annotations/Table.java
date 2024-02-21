package com.flexcore.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to specify that a class represents a table in the database.
 * The name of the table must be specified.
 * The name of the table is case-sensitive. It must match the name of the table in the database.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String name();
}
