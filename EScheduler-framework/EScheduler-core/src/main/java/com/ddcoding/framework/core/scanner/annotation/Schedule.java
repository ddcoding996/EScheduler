
package com.ddcoding.framework.core.scanner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 给方法添加该注解,代表该方法是一个可以被调度的方法.
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Schedule {

    /**
     * cron表达式.如果是非集群环境,则escheduler将会取该值作为任务的cron表达式.
     * 如果是集群环境,则该值将被忽略,使用用户在console控制台传递的cron表达式为准.
     *
     * @return cron
     */
    String cron() default "";

    /**
     * 错过的任务策略.如果是非集群环境,则escheduler将会取该值作为错过的任务策略.
     * 如果是集群环境,则该值将被忽略,使用用户在console控制台传递的策略为准.
     *
     * @return misfirePolicy
     */
    MisfirePolicy misfirePolicy() default MisfirePolicy.None;

}
