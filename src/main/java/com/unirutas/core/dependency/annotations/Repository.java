package com.unirutas.core.dependency.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Repository annotation. This annotation is used to mark a class as a repository.
 * The class must extend from CrudRepository.
 * @see com.unirutas.core.database.repository.CrudRepository
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Repository {
}
