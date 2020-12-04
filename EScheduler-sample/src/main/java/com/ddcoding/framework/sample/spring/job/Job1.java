
package com.ddcoding.framework.sample.spring.job;

import com.ddcoding.framework.core.scanner.annotation.Schedule;
import com.ddcoding.framework.sample.spring.bean.OneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class Job1 {

    @Autowired
    private OneService oneService;

    @Schedule(cron = "0/15 * * * * ?")
    public void test() {
        oneService.someServiceMethod1();
    }

}
