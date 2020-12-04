package com.ddcoding.framework.spring.bean;


import com.ddcoding.framework.common.exception.ESchedulerException;
import com.ddcoding.framework.common.helper.LoggerHelper;
import com.ddcoding.framework.core.scanner.JobScanner;
import com.ddcoding.framework.schedule.bean.JobBeanFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * spring环境下的JobBean工厂实现,所有的JobBean优先从spring IOC容器中获取.
 *
 */
public class SpringJobBeanFactory implements JobBeanFactory {

    private ApplicationContext applicationContext;

    private ClassLoader classLoader;

    /**
     * 该构造函数用于集群环境
     *
     * @throws BeansException
     */
    public SpringJobBeanFactory(ClassLoader classLoader) throws BeansException {
        this.classLoader = classLoader;
        ClassUtils.overrideThreadContextClassLoader(classLoader);
        this.applicationContext = new ClassPathXmlApplicationContext(JobScanner.APPLICATION_CONTEXT_XML_PATH);
    }

    /**
     * 该构造函数用于非集群环境
     *
     * @param applicationContext 本地的ApplicationContext上下文对象
     */
    public SpringJobBeanFactory(ApplicationContext applicationContext) {
        this.classLoader = applicationContext.getClassLoader();
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getJobBean(String className) {
        T instance;
        Class<T> clazz;
        try {
            clazz = (Class<T>) classLoader.loadClass(className);
        } catch (Throwable e) {
            throw new ESchedulerException(e);
        }
        try {
            instance = applicationContext.getBean(clazz);
        } catch (Throwable e) {
            LoggerHelper.warn("can't find instance for " + className);
            try {
                instance = clazz.newInstance();
            } catch (Throwable e1) {
                throw new ESchedulerException(e1);
            }
        }
        return instance;
    }

}
