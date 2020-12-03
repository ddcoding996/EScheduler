package com.ddcoding.framework.schedule.node;

import com.ddcoding.framework.common.helper.ClassHelper;
import com.ddcoding.framework.core.scanner.JobScanner;
import com.ddcoding.framework.core.scanner.JobScannerFactory;
import com.ddcoding.framework.schedule.AutomaticScheduleManager;
import com.ddcoding.framework.schedule.DefaultAutomaticScheduleManager;
import com.ddcoding.framework.schedule.bean.DefaultJobBeanFactory;
import com.ddcoding.framework.schedule.bean.JobBeanFactory;

/**
 * 该实现类用于非集群环境下的非spring任务执行.
 * 内部包含的自动调度器可以按照注解自动的启动的任务.
 *
 *
 */
public class SimpleLocalJobNode extends AbstractNode {

    //自动化调度器
    private AutomaticScheduleManager schedulerManager;

    public SimpleLocalJobNode(String packagesToScan) {
        JobBeanFactory jobBeanFactory = new DefaultJobBeanFactory(ClassHelper.getDefaultClassLoader());
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
