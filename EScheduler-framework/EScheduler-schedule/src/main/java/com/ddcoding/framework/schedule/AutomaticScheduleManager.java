package com.ddcoding.framework.schedule;

/**
 * 自动管理的调度管理器,主要用于本地启动时,管理任务的启动与暂停.
 *
 */
public interface AutomaticScheduleManager extends ScheduleManager {

    void startup();

    void startup(String group);

    void startup(String group, String name);

}
