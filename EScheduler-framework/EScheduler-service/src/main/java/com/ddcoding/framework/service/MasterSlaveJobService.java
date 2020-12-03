
package com.ddcoding.framework.service;

import com.ddcoding.framework.persistent.entity.MasterSlaveJob;

import java.util.List;

/**
 */
public interface MasterSlaveJobService {

    void saveJob(String jarFilePath, String packagesToScan);

    List<MasterSlaveJob> getAllJobs();

    MasterSlaveJob getJob(String group, String name, String jarFileName);

    List<String> getJarFileNameList(String group, String name);

}
