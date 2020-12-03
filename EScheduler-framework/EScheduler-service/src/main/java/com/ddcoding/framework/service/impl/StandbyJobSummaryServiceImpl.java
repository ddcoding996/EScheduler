package com.ddcoding.framework.service.impl;

import com.ddcoding.framework.api.data.StandbyJobData;
import com.ddcoding.framework.common.helper.ListHelper;
import com.ddcoding.framework.common.helper.ReflectHelper;
import com.ddcoding.framework.persistent.BaseDao;
import com.ddcoding.framework.persistent.entity.StandbyJob;
import com.ddcoding.framework.persistent.entity.StandbyJobSummary;
import com.ddcoding.framework.service.ServiceException;
import com.ddcoding.framework.service.StandbyJobLogService;
import com.ddcoding.framework.service.StandbyJobService;
import com.ddcoding.framework.service.StandbyJobSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class StandbyJobSummaryServiceImpl extends AbstractService implements StandbyJobSummaryService {

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private StandbyJobService standbyJobService;

    @Autowired
    private StandbyJobLogService standbyJobLogService;

    @Override
    public List<StandbyJobSummary> getAllJobSummaries() {
        return baseDao.getAll(StandbyJobSummary.class);
    }

    @Override
    public synchronized void saveJobSummary(StandbyJobSummary standbyJobSummary) {
        StandbyJobData standbyJobData = standbyApiFactory.jobApi().getJob(standbyJobSummary.getGroupName(), standbyJobSummary.getJobName());
        StandbyJobData.Data data;
        if (standbyJobData == null) {
            data = new StandbyJobData.Data();
        } else {
            data = standbyJobData.getData();
        }
        //set data
        ReflectHelper.copyFieldValuesSkipNull(standbyJobSummary, data);
        StandbyJob standbyJob = standbyJobService.getJob(standbyJobSummary.getGroupName(), standbyJobSummary.getJobName(), standbyJobSummary.getJarFileName());
        data.setJobOperationLogId(standbyJobLogService.saveJobLog(standbyJobSummary));
        data.setPackagesToScan(standbyJob.getPackagesToScan());
        data.setContainerType(standbyJob.getContainerType());
        //set state to Executing
        StandbyJobSummary param = new StandbyJobSummary();
        param.setGroupName(data.getGroupName());
        param.setJobName(data.getJobName());
        StandbyJobSummary standbyJobSummaryInDb = baseDao.getUnique(StandbyJobSummary.class, param);
        standbyJobSummaryInDb.setJobState("Executing");
        baseDao.update(standbyJobSummaryInDb);
        //send job
        standbyApiFactory.jobApi().saveJob(standbyJobSummary.getGroupName(), standbyJobSummary.getJobName(), data);
    }

    @Override
    public void updateJobSummary(StandbyJobData.Data data) {
        StandbyJobSummary param = new StandbyJobSummary();
        param.setGroupName(data.getGroupName());
        param.setJobName(data.getJobName());
        StandbyJobSummary standbyJobSummary = baseDao.getUnique(StandbyJobSummary.class, param);
        ReflectHelper.copyFieldValuesSkipNull(data, standbyJobSummary);
        baseDao.update(standbyJobSummary);
    }

    @Override
    public StandbyJobSummary getJobSummary(String id) {
        StandbyJobSummary standbyJobSummary = baseDao.get(StandbyJobSummary.class, id);
        StandbyJobData standbyJobData = standbyApiFactory.jobApi().getJob(standbyJobSummary.getGroupName(), standbyJobSummary.getJobName());
        if (standbyJobData != null) {
            ReflectHelper.copyFieldValues(standbyJobData.getData(), standbyJobSummary);
            standbyJobSummary.setOriginalJarFileName(standbyJobData.getData().getJarFileName());
        } else {
            List<String> jarFileNameList = standbyJobService.getJarFileNameList(standbyJobSummary.getGroupName(), standbyJobSummary.getJobName());
            if (ListHelper.isEmpty(jarFileNameList)) {
                throw new ServiceException("job detail not found.");
            } else {
                standbyJobSummary.setOriginalJarFileName(jarFileNameList.get(0));
            }
        }
        return standbyJobSummary;
    }

    @Override
    public void updateJobSummary(String id) {
        StandbyJobSummary standbyJobSummary = baseDao.get(StandbyJobSummary.class, id);
        StandbyJobData standbyJobData = standbyApiFactory.jobApi().getJob(standbyJobSummary.getGroupName(), standbyJobSummary.getJobName());
        updateJobSummary(standbyJobData.getData());
    }

}
