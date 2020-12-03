package com.ddcoding.framework.service.impl;

import com.ddcoding.framework.common.helper.JarFileHelper;
import com.ddcoding.framework.common.helper.ListHelper;
import com.ddcoding.framework.core.scanner.ApplicationClassLoader;
import com.ddcoding.framework.core.scanner.ApplicationClassLoaderFactory;
import com.ddcoding.framework.core.scanner.JobScanner;
import com.ddcoding.framework.core.scanner.JobScannerFactory;
import com.ddcoding.framework.core.scanner.job.JobDescriptor;
import com.ddcoding.framework.persistent.BaseDao;
import com.ddcoding.framework.persistent.entity.StandbyJob;
import com.ddcoding.framework.persistent.entity.StandbyJobSummary;
import com.ddcoding.framework.service.ServiceException;
import com.ddcoding.framework.service.StandbyJobService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
@Service
public class StandbyJobServiceImpl extends AbstractService implements StandbyJobService, ApplicationContextAware {

    @Autowired
    private BaseDao baseDao;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public List<StandbyJob> getAllJobs() {
        return baseDao.getAll(StandbyJob.class);
    }

    @Override
    public StandbyJob getJob(String group, String name, String jarFileName) {
        StandbyJob param = new StandbyJob();
        param.setGroupName(group);
        param.setJobName(name);
        param.setJarFileName(jarFileName);
        return baseDao.getUnique(StandbyJob.class, param);
    }

    @Override
    public void saveJob(String jarFilePath, String packagesToScan) {
        String jarFileName = JarFileHelper.getJarFileName(jarFilePath);
        StandbyJob param = new StandbyJob();
        param.setJarFileName(jarFileName);
        List<StandbyJob> standbyJobList = baseDao.getList(StandbyJob.class, param);
        if (!ListHelper.isEmpty(standbyJobList)) {
            throw new ServiceException("This jar [" + jarFileName + "] has been uploaded before.");
        }

        ApplicationClassLoader classLoader = ApplicationClassLoaderFactory.createNormalApplicationClassLoader(applicationContext.getClassLoader(), jarFilePath);
        JobScanner jobScanner = JobScannerFactory.createJarFileJobScanner(classLoader, packagesToScan, jarFilePath);
        List<JobDescriptor> jobDescriptorList = jobScanner.getJobDescriptorList();
        String mode = jobScanner.hasSpringEnvironment() ? "Spring" : "Common";
        for (JobDescriptor jobDescriptor : jobDescriptorList) {
            StandbyJob standbyJob = new StandbyJob();
            standbyJob.setGroupName(jobDescriptor.group());
            standbyJob.setJobName(jobDescriptor.name());
            standbyJob.setJarFileName(jarFileName);
            standbyJob.setPackagesToScan(packagesToScan);
            standbyJob.setContainerType(mode);
            baseDao.save(standbyJob);

            StandbyJobSummary standbyJobSummary = new StandbyJobSummary();
            standbyJobSummary.setGroupName(jobDescriptor.group());
            standbyJobSummary.setJobName(jobDescriptor.name());
            StandbyJobSummary standbyJobSummaryInDb = baseDao.getUnique(StandbyJobSummary.class, standbyJobSummary);
            if (standbyJobSummaryInDb == null) {
                standbyJobSummary.setDefaultState();
                baseDao.save(standbyJobSummary);
            }
        }
    }

    @Override
    public List<String> getJarFileNameList(String group, String name) {
        StandbyJob job = new StandbyJob();
        job.setGroupName(group);
        job.setJobName(name);
        List<StandbyJob> standbyJobList = baseDao.getList(StandbyJob.class, job, false);
        List<String> jarFileNameList = new ArrayList<>();
        if (standbyJobList == null) {
            return jarFileNameList;
        }
        jarFileNameList.addAll(standbyJobList.stream().map(StandbyJob::getJarFileName).collect(Collectors.toList()));
        return jarFileNameList;
    }

}
