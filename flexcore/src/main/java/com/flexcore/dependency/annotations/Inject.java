package com.flexcore.dependency.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to inject a dependency. This annotation is used by the DependencyInjector class to add a class to container and inject it in other classes.
 * The value of this annotation is the name of the class to inject. If the value is empty, the name of the class to inject is the type of the field.
 * This is used for different implementations of the same interface. For example, if you have an interface called "Service" and two implementations called "Service1" and "Service2", you can inject them like this:
 * <pre>
 *    {@code
 *     public class MyClass {
 *      @Inject("Service1")
 *      private Service service1;
 *      @Inject("Service2")
 *      private Service service2;
 *     }
 *    }
 * </pre>
 * If you don't specify the value, the name of the class to inject is the type of the field. For example:
 * <pre>
 *     {@code
 *     public class MyClass {
 *     @Inject
 *     private Service service;
 *     }
 *     }
 *     </pre>
 *     In this case, the name of the class to inject is "Service".
 *     If you have only one implementation of an interface, you don't need to specify the value of the annotation.
 *
 *     @see Implementation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {
    String value() default "";
}
