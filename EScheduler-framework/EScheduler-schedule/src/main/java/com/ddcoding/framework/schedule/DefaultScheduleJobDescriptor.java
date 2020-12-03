package com.ddcoding.framework.schedule;


import com.ddcoding.framework.core.scanner.annotation.MisfirePolicy;
import com.ddcoding.framework.core.scanner.annotation.Schedule;
import com.ddcoding.framework.core.scanner.job.DefaultJobDescriptor;
import com.ddcoding.framework.core.scanner.job.JobDescriptor;
import org.quartz.*;

import java.lang.reflect.Method;

/**
 * 可调度的任务描述符的默认实现.
 *
 */
public class DefaultScheduleJobDescriptor extends DefaultJobDescriptor implements ScheduleJobDescriptor {

    private JobDataMap jobDataMap;

    DefaultScheduleJobDescriptor(Class<?> clazz, Method method, boolean hasParameter, Schedule schedule) {
        super(clazz, method, hasParameter, schedule.cron(), schedule.misfirePolicy());
    }

    DefaultScheduleJobDescriptor(JobDescriptor jobDescriptor) {
        this(jobDescriptor.clazz(), jobDescriptor.method(), jobDescriptor.hasParameter(), jobDescriptor.cron(), jobDescriptor.misfirePolicy());
    }

    DefaultScheduleJobDescriptor(Class<?> clazz, Method method, boolean hasParameter, String cron, MisfirePolicy misfirePolicy) {
        super(clazz, method, hasParameter, cron, misfirePolicy);
        this.jobDataMap = new JobDataMap();
    }

    @Override
    public TriggerKey triggerKey() {
        return TriggerKey.triggerKey(name(), group());
    }

    @Override
    public JobKey jobKey() {
        return JobKey.jobKey(name(), group());
    }

    public JobDetail jobDetail() {
        return jobDetail(null);
    }

    @Override
    public JobDetail jobDetail(String jarFilePath) {
        jobDataMap.put(JobDataMapManager.JOB_DESCRIPTOR_KEY, this);
        if (jarFilePath != null) {
            jobDataMap.put(JobDataMapManager.JAR_FILE_PATH_KEY, jarFilePath);
        }
        return JobBuilder.newJob(StubJob.class)
                .withIdentity(name(), group())
                .storeDurably(true)
                .setJobData(jobDataMap)
                .build();
    }

    @Override
    public ScheduleJobDescriptor withTrigger(String cron, String misfirePolicy) {
        this.cron = cron;
        this.misfirePolicy = MisfirePolicy.valueOf(misfirePolicy);
        return this;
    }

    public Trigger trigger() {
        return TriggerBuilder.newTrigger()
                .forJob(name(), group())
                .withIdentity(name(), group())
                .withSchedule(scheduleBuilder())
                .build();
    }

    @Override
    public boolean isManualTrigger() {
        return cron == null || misfirePolicy == null;
    }

    protected ScheduleBuilder scheduleBuilder() {
        if (misfirePolicy() == MisfirePolicy.IgnoreMisfires) {
            return CronScheduleBuilder.cronSchedule(cron()).withMisfireHandlingInstructionIgnoreMisfires();
        } else if (misfirePolicy() == MisfirePolicy.DoNothing) {
            return CronScheduleBuilder.cronSchedule(cron()).withMisfireHandlingInstructionDoNothing();
        } else if (misfirePolicy() == MisfirePolicy.FireAndProceed){
            return CronScheduleBuilder.cronSchedule(cron()).withMisfireHandlingInstructionFireAndProceed();
        } else {
            return CronScheduleBuilder.cronSchedule(cron());
        }
    }

}
