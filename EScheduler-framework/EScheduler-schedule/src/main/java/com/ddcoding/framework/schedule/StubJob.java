package com.ddcoding.framework.schedule;

import com.ddcoding.framework.common.exception.ESchedulerException;
import com.ddcoding.framework.common.helper.JsonHelper;
import com.ddcoding.framework.common.helper.LoggerHelper;
import com.ddcoding.framework.core.scanner.job.JobDescriptor;
import com.ddcoding.framework.core.scanner.job.JobParameter;
import com.ddcoding.framework.schedule.bean.JobBeanFactory;
import org.quartz.*;

/**
 * 占位任务,它代表着一个由调度器添加的任务,该类会根据调度器传递的参数启动一个任务.
 *
 */
public class StubJob implements Job {

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        Scheduler scheduler = jobExecutionContext.getScheduler();
        JobDescriptor jobDescriptor = JobDataMapManager.getJobDescriptor(jobDetail);
        JobParameter jobParameter = JobDataMapManager.getJobParameter(jobDetail);
        JobBeanFactory jobBeanFactory = getJobBeanFactory(scheduler, jobDetail);
        String jobMessageString = jobDescriptor + "  JobParameter:" + JsonHelper.toJson(jobParameter);
        try {
            LoggerHelper.info("begin execute job : " + jobMessageString);
            if (jobDescriptor.hasParameter()) {
                jobDescriptor.method().invoke(jobBeanFactory.getJobBean(jobDescriptor.group()), new Object[]{jobParameter});
            } else {
                jobDescriptor.method().invoke(jobBeanFactory.getJobBean(jobDescriptor.group()), new Object[]{});
            }
            LoggerHelper.info("execute job success: " + jobMessageString);
        } catch (Exception e) {
            LoggerHelper.error("execute job failed: " + jobMessageString, e);
            throw new ESchedulerException(e);
        }
    }

    private JobBeanFactory getJobBeanFactory(Scheduler scheduler, JobDetail jobDetail) {
        ScheduleMode scheduleMode = JobDataMapManager.getScheduleMode(scheduler);
        JobBeanFactory jobBeanFactory;
        if (scheduleMode == ScheduleMode.AUTOMATIC) {
            jobBeanFactory = JobDataMapManager.getJobBeanFactory(scheduler);
        } else if (scheduleMode == ScheduleMode.MANUAL) {
            String jarFilePath = JobDataMapManager.getJarFilePath(jobDetail);
            jobBeanFactory = JobEnvironmentCache.instance().getJobBeanFactory(jarFilePath);
        } else {
            throw new ESchedulerException(new RuntimeException("Unknown schedule mode."));
        }
        return jobBeanFactory;
    }

}
