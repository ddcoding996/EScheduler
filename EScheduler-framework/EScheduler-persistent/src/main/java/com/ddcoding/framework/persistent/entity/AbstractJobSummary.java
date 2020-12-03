package com.ddcoding.framework.persistent.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 */
@MappedSuperclass
public class AbstractJobSummary extends AbstractEntity {

    private String groupName;

    private String jobName;

    private String jarFileName;

    private String packagesToScan;

    private String containerType;

    private String jobState;

    private String jobCron;

    private String misfirePolicy;

    private String originalJarFileName;

    private String jobOperation;

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setJarFileName(String jarFileName) {
        this.jarFileName = jarFileName;
    }

    public void setPackagesToScan(String packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public void setJobState(String jobState) {
        this.jobState = jobState;
    }

    public void setJobCron(String jobCron) {
        this.jobCron = jobCron;
    }

    public void setMisfirePolicy(String misfirePolicy) {
        this.misfirePolicy = misfirePolicy;
    }

    public void setOriginalJarFileName(String originalJarFileName) {
        this.originalJarFileName = originalJarFileName;
    }

    public void setJobOperation(String jobOperation) {
        this.jobOperation = jobOperation;
    }

    @Column(name = "group_name")
    public String getGroupName() {
        return groupName;
    }

    @Column(name = "job_name")
    public String getJobName() {
        return jobName;
    }

    public String getJarFileName() {
        return jarFileName;
    }

    public String getPackagesToScan() {
        return packagesToScan;
    }

    @Column(length = 30)
    public String getContainerType() {
        return containerType;
    }

    @Column(length = 30)
    public String getJobState() {
        return jobState;
    }

    @Column(length = 30)
    public String getJobCron() {
        return jobCron;
    }

    @Column(length = 30)
    public String getMisfirePolicy() {
        return misfirePolicy;
    }

    @Transient
    public String getOriginalJarFileName() {
        return originalJarFileName;
    }

    @Transient
    public String getJobOperation() {
        return jobOperation;
    }

    public void setDefaultState() {
        this.jobState = "Shutdown";
    }

    @Transient
    public String getStateLabelClass() {
        if ("Shutdown".equals(jobState)) {
            return "label-warning";
        }
        if ("Startup".equals(jobState)) {
            return "label-success";
        }
        if ("Pause".equals(jobState)) {
            return "label-inverse";
        }
        if ("Executing".equals(jobState)) {
            return "label-info";
        }
        return "";
    }

    @Transient
    public String getModeLabelClass() {
        if ("Common".equals(containerType)) {
            return "label-Info";
        }
        if ("Spring".equals(containerType)) {
            return "label-success";
        }
        return "";
    }
}
