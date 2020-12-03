package com.ddcoding.framework.service.spring;

import com.ddcoding.framework.service.DatabaseInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 数据库初始化的监听器.
 *
 */
@Component
public class DatabaseInitListener {

    @Autowired
    private DatabaseInitializationService databaseInitializationService;

    @PostConstruct
    public void listen() {
        databaseInitializationService.initialize();
    }

}
