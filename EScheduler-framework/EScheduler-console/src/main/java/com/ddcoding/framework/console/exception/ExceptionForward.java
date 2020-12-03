package com.ddcoding.framework.console.exception;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 支持异常转发
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionForward {

    /**
     * 要转发的URL
     *
     * @return URL
     */
    String value();

}
