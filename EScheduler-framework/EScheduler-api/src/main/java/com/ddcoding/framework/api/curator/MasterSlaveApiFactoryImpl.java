package com.ddcoding.framework.api.curator;

import com.ddcoding.framework.api.MasterSlaveApiFactory;
import com.ddcoding.framework.api.MasterSlaveJobApi;
import com.ddcoding.framework.api.MasterSlaveNodeApi;
import com.ddcoding.framework.api.MasterSlavePathApi;
import org.apache.curator.framework.CuratorFramework;

/**
 * 主从模式API的工厂接口实现
 *
 */
public class MasterSlaveApiFactoryImpl implements MasterSlaveApiFactory {

    private CuratorFramework client;

    public MasterSlaveApiFactoryImpl(CuratorFramework client) {
        this.client = client;
    }

    @Override
    public MasterSlavePathApi pathApi() {
        return MasterSlavePathApiImpl.INSTANCE;
    }

    @Override
    public MasterSlaveNodeApi nodeApi() {
        return new MasterSlaveNodeApiImpl(client);
    }

    @Override
    public MasterSlaveJobApi jobApi() {
        return new MasterSlaveJobApiImpl(client);
    }

}
