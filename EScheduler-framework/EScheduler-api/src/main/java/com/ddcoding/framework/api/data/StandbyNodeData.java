
package com.ddcoding.framework.api.data;

import lombok.Getter;
import lombok.Setter;
import org.apache.curator.framework.recipes.cache.ChildData;

/**
 * 主备模式ZK Node数据节点类
 *
 */
@Getter
@Setter
public class StandbyNodeData extends AbstractGenericData<StandbyNodeData, StandbyNodeData.Data> {

    public StandbyNodeData(ChildData childData) {
        super(childData);
    }

    public StandbyNodeData(String path, byte[] bytes) {
        super(path, bytes);
    }

    public StandbyNodeData(String path, Data data) {
        super(path, data);
    }

    @Setter
    @Getter
    public static class Data extends AbstractNodeData<Data> {

        public Data() {
            this(null);
        }

        public Data(String ip) {
            super(ip);
            setNodeState("Backup");
        }

    }

}
