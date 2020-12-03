package com.ddcoding.framework.api.curator;

import com.ddcoding.framework.api.MasterSlaveJobApi;
import com.ddcoding.framework.api.data.MasterSlaveJobData;
import com.ddcoding.framework.api.helper.PathHelper;
import com.ddcoding.framework.common.helper.JsonHelper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;

import java.util.List;
import java.util.stream.Collectors;

/**
 */
public class MasterSlaveJobApiImpl extends AbstractCurdApiImpl implements MasterSlaveJobApi {

    public MasterSlaveJobApiImpl(CuratorFramework client) {
        super(client);
    }

    @Override
    public List<MasterSlaveJobData> getAllJobs() {
        List<ChildData> childDataList = getChildren(getMasterSlavePathApi().getJobPath());
        return childDataList.stream().map(MasterSlaveJobData::new).collect(Collectors.toList());
    }

    @Override
    public void saveJob(String group, String name, MasterSlaveJobData.Data data) {
        data.prepareOperation();
        MasterSlaveJobData masterSlaveJobData = new MasterSlaveJobData(PathHelper.getJobPath(getMasterSlavePathApi().getJobPath(), group, name), data);
        masterSlaveJobData.getData().incrementVersion();
        if (checkExists(masterSlaveJobData.getPath())) {
            setData(masterSlaveJobData.getPath(), masterSlaveJobData.getDataBytes());
        } else {
            create(masterSlaveJobData.getPath(), JsonHelper.toBytes(masterSlaveJobData.getData()));
        }
    }

    @Override
    public void updateJob(String group, String name, MasterSlaveJobData.Data data) {
        MasterSlaveJobData masterSlaveJobData = new MasterSlaveJobData(PathHelper.getJobPath(getMasterSlavePathApi().getJobPath(), group, name), data);
        masterSlaveJobData.getData().incrementVersion();
        setData(masterSlaveJobData.getPath(), masterSlaveJobData.getDataBytes());
    }

    @Override
    public MasterSlaveJobData getJob(String group, String name) {
        return getJob(PathHelper.getJobPath(getMasterSlavePathApi().getJobPath(), group, name));
    }

    @Override
    public MasterSlaveJobData getJob(String path) {
        if (!checkExists(path)) {
            return null;
        }
        return new MasterSlaveJobData(getData(path));
    }

}
