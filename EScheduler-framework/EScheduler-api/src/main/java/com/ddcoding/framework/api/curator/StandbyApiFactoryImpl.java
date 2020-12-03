package com.ddcoding.framework.api.curator;

import com.ddcoding.framework.api.StandbyApiFactory;
import com.ddcoding.framework.api.StandbyJobApi;
import com.ddcoding.framework.api.StandbyNodeApi;
import com.ddcoding.framework.api.StandbyPathApi;
import org.apache.curator.framework.CuratorFramework;

/**
 * 主备模式API的工厂接口实现
 *
 */
public class StandbyApiFactoryImpl implements StandbyApiFactory {

    private CuratorFramework client;

    public StandbyApiFactoryImpl(CuratorFramework client) {
        this.client = client;
    }

    @Override
    public StandbyPathApi pathApi() {
        return StandbyPathApiImpl.INSTANCE;
    }

    @Override
    public StandbyNodeApi nodeApi() {
        return new StandbyNodeApiImpl(client);
    }

    @Override
    public StandbyJobApi jobApi() {
        return new StandbyJobApiImpl(client);
    }

}
