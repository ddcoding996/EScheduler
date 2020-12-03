package com.ddcoding.framework.service;

import com.ddcoding.framework.api.data.MasterSlaveJobData;
import com.ddcoding.framework.persistent.entity.MasterSlaveJobSummary;
import java.util.List;

/**
 */
public interface MasterSlaveJobSummaryService {

    List<MasterSlaveJobSummary> getAllJobSummaries();

    void saveJobSummary(MasterSlaveJobSummary standbyJobSummary);

    MasterSlaveJobSummary getJobSummary(String id);

    void updateJobSummary(MasterSlaveJobData.Data data);

    void updateJobSummary(String id);

}
