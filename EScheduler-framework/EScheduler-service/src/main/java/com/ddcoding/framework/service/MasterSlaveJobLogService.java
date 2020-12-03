
package com.ddcoding.framework.service;

import com.ddcoding.framework.api.data.MasterSlaveJobData;
import com.ddcoding.framework.persistent.entity.MasterSlaveJobLog;
import com.ddcoding.framework.persistent.entity.MasterSlaveJobSummary;

import java.util.List;

/**
 */
public interface MasterSlaveJobLogService {

    List<MasterSlaveJobLog> getAllJobLogs();

    String saveJobLog(MasterSlaveJobSummary masterSlaveJobSummary);

    void updateJobLog(MasterSlaveJobData.Data data);

}
