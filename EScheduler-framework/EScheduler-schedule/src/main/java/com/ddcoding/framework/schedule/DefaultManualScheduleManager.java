package com.ddcoding.framework.schedule;

import com.ddcoding.framework.common.exception.ESchedulerException;
import com.ddcoding.framework.common.helper.LoggerHelper;
import com.ddcoding.framework.core.scanner.annotation.MisfirePolicy;
import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import java.util.Properties;

/**
 * 手动控制的调度管理器默认实现,用于与Console交互.
 * 此类是调度器quarter类的封装
 *
 */
public class DefaultManualScheduleManager extends AbstractScheduleManager implements ManualScheduleManager {

    public DefaultManualScheduleManager(Properties properties) {
        initScheduler(properties);
        JobDataMapManager.initManualScheduler(scheduler);
    }

    @Override
    public synchronized void startupManual(String jarFilePath, String packagesToScan, boolean isSpring, String cron, String misfirePolicy) {
        for (String group : getGroupList()) {
            startupManual(jarFilePath, packagesToScan, isSpring, group, cron, misfirePolicy);
        }
    }

    @Override
    public synchronized void startupManual(String jarFilePath, String packagesToScan, boolean isSpring, String group, String cron, String misfirePolicy) {
        for (String name : getNameList(group)) {
            startupManual(jarFilePath, packagesToScan, isSpring, group, name, cron, misfirePolicy);
        }
    }

    @Override
    public synchronized void startupManual(String jarFilePath, String packagesToScan, boolean isSpring, String group, String name, String cron, String misfirePolicy) {
        try {
            JobEnvironmentCache.instance().loadJobEnvironment(jarFilePath, packagesToScan, isSpring);
        } catch (Exception e) {
            throw new ESchedulerException(e);
        }
        JobKey jobKey = JobKey.jobKey(name, group);
        ScheduleStatus scheduleStatus = jobStatusMap.get(getUniqueId(jobKey));
        ScheduleJobDescriptor jobDescriptor;
        try {
            jobDescriptor = JobEnvironmentCache.instance().findScheduleJobDescriptor(jarFilePath, group, name);
            scheduler.addJob(jobDescriptor.jobDetail(jarFilePath), true);
        } catch (SchedulerException e) {
            LoggerHelper.error("get jobDescriptor [" + group + "," + name + "] job failed.", e);
            throw new ESchedulerException(e);
        }
        if (scheduleStatus == null || scheduleStatus == ScheduleStatus.SHUTDOWN) {
            LoggerHelper.info("job [" + group + "," + name + "] now is shutdown ,begin startup.");
            try {
                scheduler.scheduleJob(jobDescriptor.withTrigger(cron, misfirePolicy).trigger());
                LoggerHelper.info("job [" + group + "," + name + "] has been started successfully.");
            } catch (SchedulerException e) {
                LoggerHelper.error("startup [" + group + "," + name + "," + scheduleStatus + "] job failed.", e);
                throw new ESchedulerException(e);
            }
        } else {
            try {
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(jobDescriptor.triggerKey());
                if (!trigger.getCronExpression().equals(cron) || trigger.getMisfireInstruction() != MisfirePolicy.valueOf(misfirePolicy).getIntValue()) {
                    scheduler.rescheduleJob(jobDescriptor.triggerKey(), jobDescriptor.withTrigger(cron, misfirePolicy).trigger());
                } else {
                    scheduler.resumeJob(jobKey);
                }
                LoggerHelper.info("job [" + group + "," + name + "] has been rescheduled.");
            } catch (SchedulerException e) {
                LoggerHelper.error("reschedule [" + group + "," + name + "," + scheduleStatus + "] job failed.", e);
                throw new ESchedulerException(e);
            }
        }
        jobStatusMap.put(getUniqueId(jobKey), ScheduleStatus.STARTUP);
    }

}