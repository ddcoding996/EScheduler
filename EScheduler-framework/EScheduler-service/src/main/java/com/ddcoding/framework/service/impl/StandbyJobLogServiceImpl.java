package com.ddcoding.framework.service.impl;

import com.ddcoding.framework.api.data.StandbyJobData;
import com.ddcoding.framework.common.helper.ReflectHelper;
import com.ddcoding.framework.common.helper.StringHelper;
import com.ddcoding.framework.persistent.BaseDao;
import com.ddcoding.framework.persistent.entity.StandbyJobLog;
import com.ddcoding.framework.persistent.entity.StandbyJobSummary;
import com.ddcoding.framework.service.StandbyJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class StandbyJobLogServiceImpl extends AbstractService implements StandbyJobLogService {

    @Autowired
    private BaseDao baseDao;

    @Override
    public List<StandbyJobLog> getAllJobLogs() {
        return baseDao.getAll(StandbyJobLog.class);
    }

    @Override
    public String saveJobLog(StandbyJobSummary standbyJobSummary) {
        StandbyJobLog standbyJobLog = new StandbyJobLog();
        ReflectHelper.copyFieldValues(standbyJobSummary, standbyJobLog);
        return baseDao.save(standbyJobLog);
    }

    @Override
    public void updateJobLog(StandbyJobData.Data data) {
        if (StringHelper.isEmpty(data.getJobOperationLogId())) {
            return;
        }
        StandbyJobLog standbyJobLog = baseDao.get(StandbyJobLog.class, data.getJobOperationLogId());
        if (standbyJobLog == null) {
            return;
        }
        int retryTimes = 10;
        ReflectHelper.copyFieldValuesSkipNull(data, standbyJobLog);
        //retry, because the update operation may occur before the save operation.
        while (retryTimes-- > 0) {
            try {
                baseDao.update(standbyJobLog);
            } catch (IllegalArgumentException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    //ignored
                }
            }
        }
        data.clearOperationLog();
        standbyApiFactory.jobApi().updateJob(data.getGroupName(), data.getJobName(), data);
    }

}
