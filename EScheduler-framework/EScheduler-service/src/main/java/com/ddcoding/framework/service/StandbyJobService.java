package com.ddcoding.framework.service;

import com.ddcoding.framework.persistent.entity.StandbyJob;
import java.util.List;

/**
 */
public interface StandbyJobService {

    void saveJob(String jarFilePath, String packagesToScan);

    List<StandbyJob> getAllJobs();

    StandbyJob getJob(String group, String name, String jarFileName);

    List<String> getJarFileNameList(String group, String name);

}
