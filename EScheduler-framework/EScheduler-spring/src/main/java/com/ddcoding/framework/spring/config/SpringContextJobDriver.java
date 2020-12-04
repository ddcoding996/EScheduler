package com.ddcoding.framework.spring.config;

import com.ddcoding.framework.core.scanner.ApplicationClassLoader;
import com.ddcoding.framework.spring.node.SimpleSpringLocalJobNode;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring环境下任务的驱动器,用于启动任务容器
 *
 */
public class SpringContextJobDriver implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @Setter
    private String packagesToScan;

    public void init() {
        ClassLoader classLoader = applicationContext.getClassLoader();
        boolean isSimpleMode = !(classLoader instanceof ApplicationClassLoader);
        //avoid dead cycle
        if (isSimpleMode) {
            new SimpleSpringLocalJobNode(applicationContext, packagesToScan).join();
        }
    }

}
