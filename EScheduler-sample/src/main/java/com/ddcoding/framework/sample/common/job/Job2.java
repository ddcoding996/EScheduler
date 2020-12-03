package com.ddcoding.framework.sample.common.job;


import com.ddcoding.framework.common.helper.LoggerHelper;
import com.ddcoding.framework.core.scanner.annotation.Schedule;

/**
 */
public class Job2 {

    @Schedule(cron = "0/5 * * * * ?")
    public void job2Test() {
        LoggerHelper.info("[job2] is running.......");
    }

}
