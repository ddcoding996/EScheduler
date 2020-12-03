package com.ddcoding.framework.service;


import com.ddcoding.framework.api.data.StandbyJobData;
import com.ddcoding.framework.persistent.entity.StandbyJobLog;
import com.ddcoding.framework.persistent.entity.StandbyJobSummary;

import java.util.List;

/**
 */
public interface StandbyJobLogService {

    List<StandbyJobLog> getAllJobLogs();

    String saveJobLog(StandbyJobSummary standbyJobSummary);

    void updateJobLog(StandbyJobData.Data data);

}
