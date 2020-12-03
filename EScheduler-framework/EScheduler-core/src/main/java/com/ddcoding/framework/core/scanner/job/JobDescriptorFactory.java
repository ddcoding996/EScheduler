package com.ddcoding.framework.core.scanner.job;

import com.ddcoding.framework.core.scanner.annotation.MisfirePolicy;
import com.ddcoding.framework.core.scanner.annotation.Schedule;
import java.lang.reflect.Method;

/**
 * 任务描述符工厂,用于创建任务描述符.
 *
 */
public final class JobDescriptorFactory {

    private JobDescriptorFactory() {}

    public static JobDescriptor jobDescriptor(Class<?> clazz, Method method, boolean hasParameter, Schedule schedule) {
        return new DefaultJobDescriptor(clazz, method, hasParameter, schedule);
    }

    public static JobDescriptor jobDescriptor(Class<?> clazz, Method method, boolean hasParameter, String cron, MisfirePolicy misfirePolicy) {
        return new DefaultJobDescriptor(clazz, method, hasParameter, cron, misfirePolicy);
    }

}
