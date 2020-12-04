package com.ddcoding.framework.sample.common.job;


import com.ddcoding.framework.common.helper.LoggerHelper;
import com.ddcoding.framework.core.scanner.annotation.Schedule;

/**
 */
public class Job1 {

    @Schedule(cron = "0/1 * * * * ?")
    public void job1Test() {
        LoggerHelper.info("[job1] is running.......");
        System.out.println("[job1] is running......."+System.currentTimeMillis());
    }

}
