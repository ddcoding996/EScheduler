package com.ddcoding.framework.service.impl;

import com.ddcoding.framework.api.data.MasterSlaveJobData;
import com.ddcoding.framework.common.helper.ListHelper;
import com.ddcoding.framework.common.helper.ReflectHelper;
import com.ddcoding.framework.persistent.BaseDao;
import com.ddcoding.framework.persistent.entity.MasterSlaveJob;
import com.ddcoding.framework.persistent.entity.MasterSlaveJobSummary;
import com.ddcoding.framework.service.MasterSlaveJobLogService;
import com.ddcoding.framework.service.MasterSlaveJobService;
import com.ddcoding.framework.service.MasterSlaveJobSummaryService;
import com.ddcoding.framework.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class MasterSlaveJobSummaryServiceImpl extends AbstractService implements MasterSlaveJobSummaryService {

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private MasterSlaveJobService masterSlaveJobService;

    @Autowired
    private MasterSlaveJobLogService masterSlaveJobLogService;

    @Override
    public List<MasterSlaveJobSummary> getAllJobSummaries() {
        return baseDao.getAll(MasterSlaveJobSummary.class);
    }

    @Override
    public synchronized void saveJobSummary(MasterSlaveJobSummary masterSlaveJobSummary) {
        MasterSlaveJobData masterSlaveJobData = masterSlaveApiFactory.jobApi().getJob(masterSlaveJobSummary.getGroupName(), masterSlaveJobSummary.getJobName());
        MasterSlaveJobData.Data data;
        if (masterSlaveJobData == null) {
            data = new MasterSlaveJobData.Data();
        } else {
            data = masterSlaveJobData.getData();
        }
        //set data
        ReflectHelper.copyFieldValuesSkipNull(masterSlaveJobSummary, data);
        MasterSlaveJob masterSlaveJob = masterSlaveJobService.getJob(masterSlaveJobSummary.getGroupName(), masterSlaveJobSummary.getJobName(), masterSlaveJobSummary.getJarFileName());
        data.setJobOperationLogId(masterSlaveJobLogService.saveJobLog(masterSlaveJobSummary));
        data.setPackagesToScan(masterSlaveJob.getPackagesToScan());
        data.setContainerType(masterSlaveJob.getContainerType());
        //set state to Executing
        MasterSlaveJobSummary param = new MasterSlaveJobSummary();
        param.setGroupName(data.getGroupName());
        param.setJobName(data.getJobName());
        MasterSlaveJobSummary masterSlaveJobSummaryInDb = baseDao.getUnique(MasterSlaveJobSummary.class, param);
        masterSlaveJobSummaryInDb.setJobState("Executing");
        baseDao.update(masterSlaveJobSummaryInDb);
        //send job
        masterSlaveApiFactory.jobApi().saveJob(masterSlaveJobSummary.getGroupName(), masterSlaveJobSummary.getJobName(), data);
    }

    @Override
    public void updateJobSummary(MasterSlaveJobData.Data data) {
        MasterSlaveJobSummary param = new MasterSlaveJobSummary();
        param.setGroupName(data.getGroupName());
        param.setJobName(data.getJobName());
        MasterSlaveJobSummary masterSlaveJobSummary = baseDao.getUnique(MasterSlaveJobSummary.class, param);
        ReflectHelper.copyFieldValuesSkipNull(data, masterSlaveJobSummary);
        baseDao.update(masterSlaveJobSummary);
    }

    @Override
    public void updateJobSummary(String id) {
        MasterSlaveJobSummary masterSlaveJobSummary = baseDao.get(MasterSlaveJobSummary.class, id);
        MasterSlaveJobData masterSlaveJobData = masterSlaveApiFactory.jobApi().getJob(masterSlaveJobSummary.getGroupName(), masterSlaveJobSummary.getJobName());
        updateJobSummary(masterSlaveJobData.getData());
    }

    @Override
    public MasterSlaveJobSummary getJobSummary(String id) {
        MasterSlaveJobSummary masterSlaveJobSummary = baseDao.get(MasterSlaveJobSummary.class, id);
        MasterSlaveJobData masterSlaveJobData = masterSlaveApiFactory.jobApi().getJob(masterSlaveJobSummary.getGroupName(), masterSlaveJobSummary.getJobName());
        if (masterSlaveJobData != null) {
            ReflectHelper.copyFieldValues(masterSlaveJobData.getData(), masterSlaveJobSummary);
            masterSlaveJobSummary.setOriginalJarFileName(masterSlaveJobData.getData().getJarFileName());
        } else {
            List<String> jarFileNameList = masterSlaveJobService.getJarFileNameList(masterSlaveJobSummary.getGroupName(), masterSlaveJobSummary.getJobName());
            if (ListHelper.isEmpty(jarFileNameList)) {
                throw new ServiceException("job detail not found.");
            } else {
                masterSlaveJobSummary.setOriginalJarFileName(jarFileNameList.get(0));
            }
        }
        return masterSlaveJobSummary;
    }

}
