package com.ddcoding.framework.api.curator;

import com.ddcoding.framework.api.StandbyJobApi;
import com.ddcoding.framework.api.data.StandbyJobData;
import com.ddcoding.framework.api.helper.PathHelper;
import com.ddcoding.framework.common.helper.JsonHelper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;

import java.util.List;
import java.util.stream.Collectors;

/**
 */
public class StandbyJobApiImpl extends AbstractCurdApiImpl implements StandbyJobApi {

    public StandbyJobApiImpl(CuratorFramework client) {
        super(client);
    }

    @Override
    public List<StandbyJobData> getAllJobs() {
        List<ChildData> childDataList = getChildren(getStandbyPathApi().getJobPath());
        return childDataList.stream().map(StandbyJobData::new).collect(Collectors.toList());
    }

    @Override
    public void saveJob(String group, String name, StandbyJobData.Data data) {
        data.prepareOperation();
        StandbyJobData standbyJobData = new StandbyJobData(PathHelper.getJobPath(getStandbyPathApi().getJobPath(), group, name), data);
        standbyJobData.getData().incrementVersion();
        if (checkExists(standbyJobData.getPath())) {
            setData(standbyJobData.getPath(), standbyJobData.getDataBytes());
        } else {
            create(standbyJobData.getPath(), JsonHelper.toBytes(standbyJobData.getData()));
        }
    }

    @Override
    public void updateJob(String group, String name, StandbyJobData.Data data) {
        StandbyJobData standbyJobData = new StandbyJobData(PathHelper.getJobPath(getStandbyPathApi().getJobPath(), group, name), data);
        standbyJobData.getData().incrementVersion();
        setData(standbyJobData.getPath(), standbyJobData.getDataBytes());
    }

    @Override
    public StandbyJobData getJob(String group, String name) {
        return getJob(PathHelper.getJobPath(getStandbyPathApi().getJobPath(), group, name));
    }

    @Override
    public StandbyJobData getJob(String path) {
        if (!checkExists(path)) {
            return null;
        }
        return new StandbyJobData(getData(path));
    }

}
