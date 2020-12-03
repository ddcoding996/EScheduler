
package com.ddcoding.framework.api.curator;


import com.ddcoding.framework.api.MasterSlavePathApi;

/**
 */
public final class MasterSlavePathApiImpl implements MasterSlavePathApi {

    public static final MasterSlavePathApi INSTANCE = new MasterSlavePathApiImpl();

    private static final String ROOT_PATH = "/job-root";

    private static final String MASTER_SLAVE_NODE_PATH = ROOT_PATH + "/master-slave-node";

    private MasterSlavePathApiImpl() {}

    @Override
    public String getSelectorPath() {
        return MASTER_SLAVE_NODE_PATH + "/selector";
    }

    @Override
    public String getInitLockPath() {
        return MASTER_SLAVE_NODE_PATH + "/initLock";
    }

    @Override
    public String getNodePath() {
        return MASTER_SLAVE_NODE_PATH + "/nodes/child";
    }

    @Override
    public String getJobPath() {
        return MASTER_SLAVE_NODE_PATH + "/jobs";
    }

}
