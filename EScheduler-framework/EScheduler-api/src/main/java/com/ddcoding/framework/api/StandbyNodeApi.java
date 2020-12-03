package com.ddcoding.framework.api;


import com.ddcoding.framework.api.data.StandbyNodeData;
import java.util.List;

/**
 * 主备模式下Node节点操作API
 *
 */
public interface StandbyNodeApi extends NodeApi {

    List<StandbyNodeData> getAllNodes();

    String saveNode(StandbyNodeData.Data data);

    void updateNode(String path, StandbyNodeData.Data data);

    StandbyNodeData getNode(String path);

}
