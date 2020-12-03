
package com.ddcoding.framework.api;

import com.ddcoding.framework.api.data.MasterSlaveJobData;

import java.util.List;

/**
 * 主从模式下Job节点操作API
 *
 */
public interface MasterSlaveJobApi {

    List<MasterSlaveJobData> getAllJobs();

    void saveJob(String group, String name, MasterSlaveJobData.Data data);

    void updateJob(String group, String name, MasterSlaveJobData.Data data);

    MasterSlaveJobData getJob(String group, String name);

    MasterSlaveJobData getJob(String path);

}
