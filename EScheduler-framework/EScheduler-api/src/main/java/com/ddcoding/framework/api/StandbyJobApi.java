package com.ddcoding.framework.api;


import com.ddcoding.framework.api.data.StandbyJobData;

import java.util.List;

/**
 * 主备模式下Job节点操作API
 *
 */
public interface StandbyJobApi {

    List<StandbyJobData> getAllJobs();

    void saveJob(String group, String name, StandbyJobData.Data data);

    void updateJob(String group, String name, StandbyJobData.Data data);

    StandbyJobData getJob(String group, String name);

    StandbyJobData getJob(String path);

}
