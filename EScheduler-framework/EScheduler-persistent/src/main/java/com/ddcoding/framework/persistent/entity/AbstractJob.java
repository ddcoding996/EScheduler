package com.ddcoding.framework.persistent.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 */
@MappedSuperclass
public class AbstractJob extends AbstractEntity {

    private String groupName;

    private String jobName;

    private String jarFileName;

    private String packagesToScan;

    private String containerType;

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

    @Column(name = "jar_file_name")
    public String getJarFileName() {
        return jarFileName;
    }

    @Column(name = "group_name")
    public String getGroupName() {
        return groupName;
    }

    @Column(name = "job_name")
    public String getJobName() {
        return jobName;
    }

    public String getPackagesToScan() {
        return packagesToScan;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
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
