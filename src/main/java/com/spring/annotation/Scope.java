package com.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scope {

    /**
     * 类型
     */
    String value() default "";

    interface Scopes {
        String SINGLETON = "singleton";
        String PROTOTYPE = "prototype";
    }

}
