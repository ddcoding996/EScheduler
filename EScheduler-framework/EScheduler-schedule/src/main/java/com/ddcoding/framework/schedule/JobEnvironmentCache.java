
package com.ddcoding.framework.schedule;

import com.ddcoding.framework.common.exception.ESchedulerException;
import com.ddcoding.framework.core.scanner.ApplicationClassLoaderFactory;
import com.ddcoding.framework.core.scanner.JobScanner;
import com.ddcoding.framework.core.scanner.JobScannerFactory;
import com.ddcoding.framework.core.scanner.job.JobDescriptor;
import com.ddcoding.framework.schedule.bean.JobBeanFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存所有加载过的Job运行环境
 *
 */
public class JobEnvironmentCache {

    /**
     * ====================================
     * 单例模式开始
     * ====================================
     */

    private static JobEnvironmentCache jobEnvironmentCache = new JobEnvironmentCache();

    public static JobEnvironmentCache instance() {
        return jobEnvironmentCache;
    }

    private Map<String, JobBeanFactory> jobBeanFactoryMap;

    private Map<String, List<JobDescriptor>> jobDescriptorListMap;

    private JobEnvironmentCache() {
        this.jobBeanFactoryMap = new HashMap<>();
        this.jobDescriptorListMap = new HashMap<>();
    }

    /**
     * =====================================
     * 单例模式结束
     * =====================================
     */

    /**
     * 获取JobBean工厂
     *
     * @param jarFilePath jar文件路径
     * @return JobBean工厂
     */
    public JobBeanFactory getJobBeanFactory(String jarFilePath) {
        return jobBeanFactoryMap.get(jarFilePath);
    }

    /**
     * 查找可调度的任务描述符
     *
     * @param jarFilePath jar包路径
     * @param group 组名
     * @param name 名称
     * @return 若找到则返回一个ScheduleJobDescriptor实例,否则抛出异常
     */
    public ScheduleJobDescriptor findScheduleJobDescriptor(String jarFilePath, String group, String name) {
        List<JobDescriptor> jobDescriptorList = jobDescriptorListMap.get(jarFilePath);
        if (jobDescriptorList == null) {
            throw new ESchedulerException(new IllegalStateException("job descriptor list can't be null."));
        }
        for (JobDescriptor jobDescriptorInner : jobDescriptorList) {
            if (jobDescriptorInner.group().equals(group) && jobDescriptorInner.name().equals(name)) {
                return new DefaultScheduleJobDescriptor(jobDescriptorInner);
            }
        }
        throw new ESchedulerException(new RuntimeException("can't find SchedulerJobDescriptor for [" + group + "." + name + "]"));
    }

    /**
     * 创建Job的运行时环境,加载相应的Jar包资源.
     *
     * @param jarFilePath jar包本地路径
     * @param packagesToScan 需要扫描的包
     * @param isSpring 是否spring环境
     * @throws Exception 当出现未检查的异常时抛出
     */
    public void loadJobEnvironment(String jarFilePath, String packagesToScan, boolean isSpring) throws Exception {
        JobBeanFactory jobBeanFactory = jobBeanFactoryMap.get(jarFilePath);
        if (jobBeanFactory != null) {
            return;
        }
        synchronized (jobBeanFactoryMap) {
            jobBeanFactory = jobBeanFactoryMap.get(jarFilePath);
            if (jobBeanFactory != null) {
                return;
            }
            jobBeanFactory = createJobBeanFactory(jarFilePath, isSpring);
            jobBeanFactoryMap.put(jarFilePath, jobBeanFactory);
            ClassLoader classLoader = ApplicationClassLoaderFactory.getJarApplicationClassLoader(jarFilePath);
            JobScanner jobScanner = JobScannerFactory.createJarFileJobScanner(classLoader, packagesToScan, jarFilePath);
            jobDescriptorListMap.put(jarFilePath, jobScanner.getJobDescriptorList());
        }
    }

    /**
     * 创建缓存JobBean实例的工厂
     *
     * @param jarFilePath Jar包本地路径
     * @param isSpring 是否spring环境
     * @return 创建好的JobBean工厂
     * @throws Exception 当出现未检查的异常时抛出
     */
    protected JobBeanFactory createJobBeanFactory(String jarFilePath, boolean isSpring) throws Exception {
        String jobBeanFactoryClassName;
        if (isSpring) {
            jobBeanFactoryClassName = "com.zuoxiaolong.niubi.job.spring.bean.SpringJobBeanFactory";
        } else {
            jobBeanFactoryClassName = "com.zuoxiaolong.niubi.job.scheduler.bean.DefaultJobBeanFactory";
        }
        ClassLoader jarApplicationClassLoader = ApplicationClassLoaderFactory.getJarApplicationClassLoader(jarFilePath);
        Class<? extends JobBeanFactory> jobBeanFactoryClass = (Class<? extends JobBeanFactory>) jarApplicationClassLoader.loadClass(jobBeanFactoryClassName);
        Class<?>[] parameterTypes = new Class[]{ClassLoader.class};
        Constructor<? extends JobBeanFactory> jobBeanFactoryConstructor = jobBeanFactoryClass.getConstructor(parameterTypes);
        return jobBeanFactoryConstructor.newInstance(jarApplicationClassLoader);
    }

}
