package com.ddcoding.framework.schedule;

import com.ddcoding.framework.core.scanner.job.JobDescriptor;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

/**
 * 可调度的任务描述符,将任务描述符与quartz集成.
 *
 *
 * @see JobDescriptor
 * @see DefaultScheduleJobDescriptor
 *
 */
public interface ScheduleJobDescriptor extends JobDescriptor {

    boolean isManualTrigger();

    JobDetail jobDetail();

    JobDetail jobDetail(String jarFilePath);

    Trigger trigger();

    ScheduleJobDescriptor withTrigger(String cron, String misfirePolicy);

    TriggerKey triggerKey();

    JobKey jobKey();

}
