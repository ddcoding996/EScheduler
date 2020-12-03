package com.ddcoding.framework.schedule;

/**
 * 手动控制的调度管理器.
 * 在调度一个任务时,需要制定任务的运行环境和cron表达式
 *
 */
public interface ManualScheduleManager extends ScheduleManager {

    void startupManual(String jarFilePath, String packagesToScan, boolean isSpring, String cron, String misfirePolicy);

    void startupManual(String jarFilePath, String packagesToScan, boolean isSpring, String group, String cron, String misfirePolicy);

    void startupManual(String jarFilePath, String packagesToScan, boolean isSpring, String group, String name, String cron, String misfirePolicy);

}
