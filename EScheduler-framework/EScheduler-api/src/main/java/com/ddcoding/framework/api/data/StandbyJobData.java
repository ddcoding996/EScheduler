package com.ddcoding.framework.api.data;

import lombok.Getter;
import lombok.Setter;
import org.apache.curator.framework.recipes.cache.ChildData;

/**
 * 主备模式ZK Job数据节点类
 *
 */
@Setter
@Getter
public class StandbyJobData extends AbstractGenericData<StandbyJobData, StandbyJobData.Data> {

    public StandbyJobData(ChildData childData) {
        super(childData);
    }

    public StandbyJobData(String path, byte[] bytes) {
        super(path, bytes);
    }

    public StandbyJobData(String path, Data data) {
        super(path, data);
    }

    @Setter
    @Getter
    public static class Data extends AbstractJobData<Data>{

    }

}
