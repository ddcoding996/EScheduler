
package com.ddcoding.framework.persistent.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 */
@MappedSuperclass
public class AbstractJobLog extends AbstractEntity {

    private String groupName;

    private String jobName;

    private String jobOperation;

    private String originalJarFileName;

    private String jobCron;

    private String jarFileName;

    private String containerType;

    private String misfirePolicy;

    private String operationResult = "Waiting";

    private String errorMessage;

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setJobOperation(String jobOperation) {
        this.jobOperation = jobOperation;
    }

    public void setOriginalJarFileName(String originalJarFileName) {
        this.originalJarFileName = originalJarFileName;
    }

    public void setJobCron(String jobCron) {
        this.jobCron = jobCron;
    }

    public void setJarFileName(String jarFileName) {
        this.jarFileName = jarFileName;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public void setMisfirePolicy(String misfirePolicy) {
        this.misfirePolicy = misfirePolicy;
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getJobName() {
        return jobName;
    }

    @Column(length = 30)
    public String getJobOperation() {
        return jobOperation;
    }

    public String getOriginalJarFileName() {
        return originalJarFileName;
    }

    @Column(length = 30)
    public String getJobCron() {
        return jobCron;
    }

    public String getJarFileName() {
        return jarFileName;
    }

    @Column(length = 30)
    public String getContainerType() {
        return containerType;
    }

    @Column(length = 30)
    public String getMisfirePolicy() {
        return misfirePolicy;
    }

    @Column(length = 30)
    public String getOperationResult() {
        return operationResult;
    }

    @Column(length = 5000)
    public String getErrorMessage() {
        return errorMessage;
    }

    @Transient
    public String getOperationLabelClass() {
        if ("Start".equals(jobOperation)) {
            return "label-warning";
        }
        if ("Restart".equals(jobOperation)) {
            return "label-success";
        }
        if ("Pause".equals(jobOperation)) {
            return "label-important";
        }
        return "";
    }

    @Transient
    public String getOperationResultLabelClass() {
        if ("Waiting".equals(operationResult)) {
            return "label-warning";
        }
        if ("Success".equals(operationResult)) {
            return "label-success";
        }
        if ("Failed".equals(operationResult)) {
            return "label-important";
        }
        return "";
    }

}
