
package com.ddcoding.framework.service.impl;

import com.ddcoding.framework.common.helper.JarFileHelper;
import com.ddcoding.framework.common.helper.ListHelper;
import com.ddcoding.framework.core.scanner.ApplicationClassLoader;
import com.ddcoding.framework.core.scanner.ApplicationClassLoaderFactory;
import com.ddcoding.framework.core.scanner.JobScanner;
import com.ddcoding.framework.core.scanner.JobScannerFactory;
import com.ddcoding.framework.core.scanner.job.JobDescriptor;
import com.ddcoding.framework.persistent.BaseDao;
import com.ddcoding.framework.persistent.entity.MasterSlaveJob;
import com.ddcoding.framework.persistent.entity.MasterSlaveJobSummary;
import com.ddcoding.framework.service.MasterSlaveJobService;
import com.ddcoding.framework.service.ServiceException;
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
public class MasterSlaveJobServiceImpl extends AbstractService implements MasterSlaveJobService, ApplicationContextAware {

    @Autowired
    private BaseDao baseDao;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public List<MasterSlaveJob> getAllJobs() {
        return baseDao.getAll(MasterSlaveJob.class);
    }

    @Override
    public MasterSlaveJob getJob(String group, String name, String jarFileName) {
        MasterSlaveJob param = new MasterSlaveJob();
        param.setGroupName(group);
        param.setJobName(name);
        param.setJarFileName(jarFileName);
        return baseDao.getUnique(MasterSlaveJob.class, param);
    }

    @Override
    public void saveJob(String jarFilePath, String packagesToScan) {
        String jarFileName = JarFileHelper.getJarFileName(jarFilePath);
        MasterSlaveJob param = new MasterSlaveJob();
        param.setJarFileName(jarFileName);
        List<MasterSlaveJob> masterSlaveJobList = baseDao.getList(MasterSlaveJob.class, param);
        if (!ListHelper.isEmpty(masterSlaveJobList)) {
            throw new ServiceException("This jar [" + jarFileName + "] has been uploaded before.");
        }

        ApplicationClassLoader classLoader = ApplicationClassLoaderFactory.createNormalApplicationClassLoader(applicationContext.getClassLoader(), jarFilePath);
        JobScanner jobScanner = JobScannerFactory.createJarFileJobScanner(classLoader, packagesToScan, jarFilePath);
        List<JobDescriptor> jobDescriptorList = jobScanner.getJobDescriptorList();
        String mode = jobScanner.hasSpringEnvironment() ? "Spring" : "Common";
        for (JobDescriptor jobDescriptor : jobDescriptorList) {
            MasterSlaveJob masterSlaveJob = new MasterSlaveJob();
            masterSlaveJob.setGroupName(jobDescriptor.group());
            masterSlaveJob.setJobName(jobDescriptor.name());
            masterSlaveJob.setJarFileName(jarFileName);
            masterSlaveJob.setPackagesToScan(packagesToScan);
            masterSlaveJob.setContainerType(mode);
            baseDao.save(masterSlaveJob);

            MasterSlaveJobSummary masterSlaveJobSummary = new MasterSlaveJobSummary();
            masterSlaveJobSummary.setGroupName(jobDescriptor.group());
            masterSlaveJobSummary.setJobName(jobDescriptor.name());
            MasterSlaveJobSummary masterSlaveJobSummaryInDb = baseDao.getUnique(MasterSlaveJobSummary.class, masterSlaveJobSummary);
            if (masterSlaveJobSummaryInDb == null) {
                masterSlaveJobSummary.setDefaultState();
                baseDao.save(masterSlaveJobSummary);
            }
        }
    }

    @Override
    public List<String> getJarFileNameList(String group, String name) {
        MasterSlaveJob job = new MasterSlaveJob();
        job.setGroupName(group);
        job.setJobName(name);
        List<MasterSlaveJob> masterSlaveJobList = baseDao.getList(MasterSlaveJob.class, job, false);
        List<String> jarFileNameList = new ArrayList<>();
        if (masterSlaveJobList == null) {
            return jarFileNameList;
        }
        jarFileNameList.addAll(masterSlaveJobList.stream().map(MasterSlaveJob::getJarFileName).collect(Collectors.toList()));
        return jarFileNameList;
    }

}
