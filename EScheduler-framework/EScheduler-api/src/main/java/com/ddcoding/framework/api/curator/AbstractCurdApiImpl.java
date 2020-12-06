package com.ddcoding.framework.api.curator;

import com.ddcoding.framework.api.MasterSlavePathApi;
import com.ddcoding.framework.api.StandbyPathApi;
import com.ddcoding.framework.common.exception.ESchedulerException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象的ZK CURD操作API实现类
 *
 */
public abstract class AbstractCurdApiImpl {

    private static final Stat EMPTY_STAT = new Stat();

    private CuratorFramework client;

    private StandbyPathApi standbyPathApi = StandbyPathApiImpl.INSTANCE;

    private MasterSlavePathApi masterSlavePathApi = MasterSlavePathApiImpl.INSTANCE;

    public AbstractCurdApiImpl(CuratorFramework client) {
        this.client = client;
    }

    /**
     * 子类可以通过该方法获取curator客户端
     *
     * @return curator客户端
     */
    protected CuratorFramework getClient() {
        return client;
    }

    /**
     * 获取主备模式下的PATH API
     *
     * @return 主备模式下的PATH API
     */
    protected StandbyPathApi getStandbyPathApi() {
        return standbyPathApi;
    }

    /**
     * 获取主从模式下的PATH API
     *
     * @return 主从模式下的PATH API
     */
    protected MasterSlavePathApi getMasterSlavePathApi() {
        return masterSlavePathApi;
    }

    /**
     * 获取该path下的所有子节点
     *
     * @param path 父节点路径
     * @return 所有子节点
     */
    protected List<ChildData> getChildren(String path) {
        if (!checkExists(path)) {
            return new ArrayList<>();
        }
        try {
            List<ChildData> childDataList = new ArrayList<>();
            List<String> children = client.getChildren().forPath(path);
            childDataList.addAll(children.stream().map(child -> getData(path + "/" + child)).collect(Collectors.toList()));
            return childDataList;
        } catch (Exception e) {
            throw new ESchedulerException(e);
        }
    }

    /**
     * 获取该节点的数据
     *
     * @param path 节点路径
     * @return 节点数据
     */
    protected ChildData getData(String path) {
        try {
            return new ChildData(path, EMPTY_STAT, client.getData().forPath(path));
        } catch (Exception e) {
            throw new ESchedulerException(e);
        }
    }

    /**
     * 检查该节点是否存在
     *
     * @param path 节点路径
     * @return 如果存在返回true,否则为false
     */
    protected boolean checkExists(String path) {
        try {
            return client.checkExists().forPath(path) != null;
        } catch (Exception e) {
            throw new ESchedulerException(e);
        }
    }

    /**
     * 创建一个节点
     *
     * @param path 节点路径
     * @param data 节点数据
     * @return 节点路径
     */
    protected String create(String path, byte[] data) {
        try {
            return getClient().create().creatingParentsIfNeeded().forPath(path, data);
        } catch (Exception e) {
            throw new ESchedulerException(e);
        }
    }

    protected String createWithProtection(String path, byte[] data) {
        try {
            return getClient().create().creatingParentsIfNeeded().withProtection().forPath(path, data);
        } catch (Exception e) {
            throw new ESchedulerException(e);
        }
    }

    protected String createPersistentWithProtection(String path, byte[] data) {
        try {
            return getClient().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, data);
        } catch (Exception e) {
            throw new ESchedulerException(e);
        }
    }

    protected String createPersistentSequentialWithProtection(String path, byte[] data) {
        try {
            return getClient().create().creatingParentsIfNeeded().withProtection().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path, data);
        } catch (Exception e) {
            throw new ESchedulerException(e);
        }
    }

    protected String createPersistentSequential(String path, byte[] data) {
        try {
            return getClient().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path, data);
        } catch (Exception e) {
            throw new ESchedulerException(e);
        }
    }

    protected String createEphemeralSequential(String path, byte[] data) {
        try {
            return getClient().create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, data);
        } catch (Exception e) {
            throw new ESchedulerException(e);
        }
    }

    protected void delete(String path) {
        try {
            getClient().delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            throw new ESchedulerException(e);
        }
    }

    /**
     * 更新元数据到zk
     * @param path
     * @param data
     * @return
     */
    protected Stat setData(String path, byte[] data) {
        try {
            return getClient().setData().forPath(path, data);
        } catch (Exception e) {
            throw new ESchedulerException(e);
        }
    }

}
