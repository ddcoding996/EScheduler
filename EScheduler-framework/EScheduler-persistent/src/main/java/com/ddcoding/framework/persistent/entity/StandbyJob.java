package com.ddcoding.framework.persistent.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_STANDBY_JOB", columnNames = {"jar_file_name","group_name","job_name"})})
public class StandbyJob extends AbstractJob {

}
