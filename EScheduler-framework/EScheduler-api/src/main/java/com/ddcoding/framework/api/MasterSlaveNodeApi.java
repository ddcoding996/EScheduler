package com.ddcoding.framework.api;


import com.ddcoding.framework.api.data.MasterSlaveNodeData;

import java.util.List;

/**
 * 主从模式下Node节点操作API
 *
 */
public interface MasterSlaveNodeApi extends NodeApi {

    List<MasterSlaveNodeData> getAllNodes();

    String saveNode(MasterSlaveNodeData.Data data);

    void updateNode(String path, MasterSlaveNodeData.Data data);

    MasterSlaveNodeData getNode(String path);

}
