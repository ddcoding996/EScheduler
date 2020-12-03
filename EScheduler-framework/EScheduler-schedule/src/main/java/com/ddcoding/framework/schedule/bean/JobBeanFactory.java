package com.ddcoding.framework.schedule.bean;

/**
 * JobBean工厂接口,该接口设计是为了更好的兼容spring容器的JobBean管理
 *
 *
 * @see DefaultJobBeanFactory
 *
 */
public interface JobBeanFactory {

    <T> T getJobBean(String className);

}
