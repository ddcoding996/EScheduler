package com.ddcoding.framework.api.curator;


import com.ddcoding.framework.api.StandbyPathApi;

/**
 */
public final class StandbyPathApiImpl implements StandbyPathApi {

    public static final StandbyPathApi INSTANCE = new StandbyPathApiImpl();

    private static final String ROOT_PATH = "/job-root";

    private static final String STANDBY_NODE_PATH = ROOT_PATH + "/standby-node";

    private StandbyPathApiImpl() {}

    @Override
    public String getInitLockPath() {
        return STANDBY_NODE_PATH + "/initLock";
    }

    @Override
    public String getNodePath() {
        return STANDBY_NODE_PATH + "/nodes/child";
    }

    @Override
    public String getSelectorPath() {
        return STANDBY_NODE_PATH + "/selector";
    }

    @Override
    public String getJobPath() {
        return STANDBY_NODE_PATH + "/jobs";
    }

}
