package com.ddcoding.framework.service.impl;

import com.ddcoding.framework.api.data.MasterSlaveJobData;
import com.ddcoding.framework.common.helper.ReflectHelper;
import com.ddcoding.framework.common.helper.StringHelper;
import com.ddcoding.framework.persistent.BaseDao;
import com.ddcoding.framework.persistent.entity.MasterSlaveJobLog;
import com.ddcoding.framework.persistent.entity.MasterSlaveJobSummary;
import com.ddcoding.framework.service.MasterSlaveJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class MasterSlaveJobLogServiceImpl extends AbstractService implements MasterSlaveJobLogService {

    @Autowired
    private BaseDao baseDao;

    @Override
    public List<MasterSlaveJobLog> getAllJobLogs() {
        return baseDao.getAll(MasterSlaveJobLog.class);
    }

    @Override
    public String saveJobLog(MasterSlaveJobSummary masterSlaveJobSummary) {
        MasterSlaveJobLog masterSlaveJobLog = new MasterSlaveJobLog();
        ReflectHelper.copyFieldValues(masterSlaveJobSummary, masterSlaveJobLog);
        return baseDao.save(masterSlaveJobLog);
    }

    @Override
    public void updateJobLog(MasterSlaveJobData.Data data) {
        if (StringHelper.isEmpty(data.getJobOperationLogId())) {
            return;
        }
        MasterSlaveJobLog masterSlaveJobLog = baseDao.get(MasterSlaveJobLog.class, data.getJobOperationLogId());
        if (masterSlaveJobLog == null) {
            return;
        }
        int retryTimes = 10;
        ReflectHelper.copyFieldValuesSkipNull(data, masterSlaveJobLog);
        //retry, because the update operation may occur before the save operation.
        while (retryTimes-- > 0) {
            try {
                baseDao.update(masterSlaveJobLog);
            } catch (IllegalArgumentException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    //ignored
                }
            }
        }
        data.clearOperationLog();
        masterSlaveApiFactory.jobApi().updateJob(data.getGroupName(), data.getJobName(), data);
    }

}
