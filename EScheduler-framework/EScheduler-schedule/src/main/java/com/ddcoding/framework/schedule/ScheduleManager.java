
package com.ddcoding.framework.schedule;

import java.util.List;

/**
 * 调度管理器接口.定义了一个基本的调度管理器应该具备的行为.
 *
 */
public interface ScheduleManager {

    void shutdown();

    void shutdown(String group);

    void shutdown(String group, String name);

    List<String> getGroupList();

    List<String> getNameList(String group);

    ScheduleStatus getScheduleStatus(String group, String name);

}
