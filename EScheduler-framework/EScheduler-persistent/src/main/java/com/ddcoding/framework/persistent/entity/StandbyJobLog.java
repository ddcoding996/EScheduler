package com.ddcoding.framework.persistent.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;

/**
 */
@Entity
@DynamicInsert
@DynamicUpdate
public class StandbyJobLog extends AbstractJobLog {

}
