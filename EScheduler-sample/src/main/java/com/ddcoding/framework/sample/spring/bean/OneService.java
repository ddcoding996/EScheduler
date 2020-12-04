
package com.ddcoding.framework.sample.spring.bean;

import com.ddcoding.framework.common.helper.LoggerHelper;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OneService {

    public void someServiceMethod1() {
        LoggerHelper.info("[job1] invoke [serviceMethod1] successfully......");
    }

    public void someServiceMethod2() {
        LoggerHelper.info("[job2] invoke [serviceMethod2] successfully......");
    }

}
