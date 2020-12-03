
package com.ddcoding.framework.api.curator;

import com.ddcoding.framework.api.StandbyNodeApi;
import com.ddcoding.framework.api.data.StandbyNodeData;
import com.ddcoding.framework.api.helper.PathHelper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;

import java.util.List;
import java.util.stream.Collectors;

/**
 */
public class StandbyNodeApiImpl extends AbstractCurdApiImpl implements StandbyNodeApi {

    public StandbyNodeApiImpl(CuratorFramework client) {
        super(client);
    }

    @Override
    public List<StandbyNodeData> getAllNodes() {
        List<ChildData> childDataList = getChildren(PathHelper.getParentPath(getStandbyPathApi().getNodePath()));
        return childDataList.stream().map(StandbyNodeData::new).collect(Collectors.toList());
    }

    @Override
    public String saveNode(StandbyNodeData.Data data) {
        StandbyNodeData standbyNodeData = new StandbyNodeData(getStandbyPathApi().getNodePath(), data);
        return createEphemeralSequential(standbyNodeData.getPath(), standbyNodeData.getDataBytes());
    }

    @Override
    public void updateNode(String path, StandbyNodeData.Data data) {
        StandbyNodeData standbyNodeData = new StandbyNodeData(path, data);
        setData(standbyNodeData.getPath(), standbyNodeData.getDataBytes());
    }

    @Override
    public StandbyNodeData getNode(String path) {
        return new StandbyNodeData(getData(path));
    }


    @Override
    public void deleteNode(String path) {
        delete(path);
    }

}
