package com.ddcoding.framework.sample.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * use to test jobs.
 *
 */
public class Test {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("applicationContext.xml");
    }

}
