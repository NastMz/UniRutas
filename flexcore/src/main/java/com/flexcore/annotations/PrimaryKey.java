package com.flexcore.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to specify the primary key of a table in the database.
 * The value of the annotation is the name of the column that make up the primary key.
 * This annotation can only be used in fields of engine String.
 * Also, you can create a primary key composed of several columns, for this you only need to annotate the fields with this annotation.
 * The name is case-sensitive and must match the name of the column in the database.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
    String name();
}