package com.ddcoding.framework.spring.node;

import com.ddcoding.framework.common.helper.ClassHelper;
import com.ddcoding.framework.core.scanner.JobScanner;
import com.ddcoding.framework.core.scanner.JobScannerFactory;
import com.ddcoding.framework.schedule.AutomaticScheduleManager;
import com.ddcoding.framework.schedule.DefaultAutomaticScheduleManager;
import com.ddcoding.framework.schedule.bean.JobBeanFactory;
import com.ddcoding.framework.schedule.node.AbstractNode;
import com.ddcoding.framework.spring.bean.SpringJobBeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * 用于非集群情况下的spring环境.
 * 该节点包含了一个自动调度的管理器,它将会按照任务注解自动的启动所有的任务.
 *
 */
public class SimpleSpringLocalJobNode extends AbstractNode {

    private AutomaticScheduleManager schedulerManager;

    public SimpleSpringLocalJobNode(ApplicationContext applicationContext, String packagesToScan) {
        ClassHelper.overrideThreadContextClassLoader(applicationContext.getClassLoader());
        JobBeanFactory jobBeanFactory = new SpringJobBeanFactory(applicationContext);
        JobScanner jobScanner = JobScannerFactory.createClasspathJobScanner(ClassHelper.getDefaultClassLoader(), packagesToScan);
        schedulerManager = new DefaultAutomaticScheduleManager(jobBeanFactory, jobScanner.getJobDescriptorList());
    }

    @Override
    public void join() {
        schedulerManager.startup();
    }

    @Override
    public void exit() {
        schedulerManager.shutdown();
    }

}
