package com.ddcoding.framework.core.scanner.job;

import com.ddcoding.framework.core.scanner.annotation.MisfirePolicy;
import java.lang.reflect.Method;

/**
 * 任务描述符.每一个任务描述符代表着一个任务.
 *
 */
public interface JobDescriptor {

    String group();

    String name();

    Method method();

    boolean hasParameter();

    Class<?> clazz();

    String cron();

    MisfirePolicy misfirePolicy();

}
