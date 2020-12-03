
package com.ddcoding.framework.service.impl;


import com.ddcoding.framework.api.MasterSlaveApiFactory;
import com.ddcoding.framework.api.StandbyApiFactory;
import com.ddcoding.framework.api.curator.MasterSlaveApiFactoryImpl;
import com.ddcoding.framework.api.curator.StandbyApiFactoryImpl;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 抽象的服务实现类.
 * 它包含了主从和主备API工厂实例,可以帮助子类方便的进行ZK操作.
 *
 */
@Service
public class AbstractService implements InitializingBean {

    @Autowired
    private CuratorFramework client;

    protected StandbyApiFactory standbyApiFactory;

    protected MasterSlaveApiFactory masterSlaveApiFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.standbyApiFactory = new StandbyApiFactoryImpl(client);
        this.masterSlaveApiFactory = new MasterSlaveApiFactoryImpl(client);
    }

}
