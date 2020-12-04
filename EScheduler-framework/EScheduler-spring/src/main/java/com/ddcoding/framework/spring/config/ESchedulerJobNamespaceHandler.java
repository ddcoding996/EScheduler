package com.ddcoding.framework.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 命名空间处理器
 *
 */
public class ESchedulerJobNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("job-driven", new JobDrivenBeanDefinitionParser());
    }

}
