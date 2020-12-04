package com.ddcoding.framework.cluster.node;

import com.ddcoding.framework.api.StandbyApiFactory;
import com.ddcoding.framework.api.curator.StandbyApiFactoryImpl;
import com.ddcoding.framework.api.data.StandbyJobData;
import com.ddcoding.framework.api.data.StandbyNodeData;
import com.ddcoding.framework.api.helper.EventHelper;
import com.ddcoding.framework.cluster.startup.Bootstrap;
import com.ddcoding.framework.common.exception.ESchedulerException;
import com.ddcoding.framework.common.helper.*;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;

import java.util.ArrayList;
import java.util.List;

/**
 * 主备模式的集群实现.
 *
 */
public class StandbyNode extends AbstractClusterJobNode {

    private final LeaderSelector leaderSelector;

    private InterProcessLock initLock;

    private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, Integer.MAX_VALUE);

    private CuratorFramework client;

    private PathChildrenCache jobCache;

    private StandbyApiFactory standbyApiFactory;

    private String nodePath;

    public StandbyNode() {
        //初始化zk的客户端Curator，操作zk
        this.client = CuratorFrameworkFactory.newClient(Bootstrap.getZookeeperAddresses(), retryPolicy);
        this.client.start();

        //操作
        this.standbyApiFactory = new StandbyApiFactoryImpl(client);
        //zk初始化lock，写锁，排他锁
        this.initLock = new InterProcessMutex(client, standbyApiFactory.pathApi().getInitLockPath());
        try {
            this.initLock.acquire();//获取zk锁
            LoggerHelper.info("get init lock... begin init jobs.");
            initJobs(); //初始化zk的job的数据
            LoggerHelper.info("init jobs successfully.");
        } catch (Exception e) {
            throw new ESchedulerException(e);
        } finally {
            try {
                this.initLock.release();//释放zk的锁
            } catch (Exception e) {
                throw new ESchedulerException(e);
            }
        }
        //保存启动的节点的ip到zk
        this.nodePath = standbyApiFactory.nodeApi().saveNode(new StandbyNodeData.Data(getNodeIp()));
        //保存工作job的zk的path路径
        this.jobCache = new PathChildrenCache(client, standbyApiFactory.pathApi().getJobPath(), true);
        //注册监听，数据有变化触发，为主备做好准备
        this.jobCache.getListenable().addListener(new JobCacheListener());
        //获取主节点，做好高可用StandbyLeadershipSelectorListener
        this.leaderSelector = new LeaderSelector(client, standbyApiFactory.pathApi().getSelectorPath(), new StandbyLeadershipSelectorListener());
        //调用此方法，返回主节点
        this.leaderSelector.autoRequeue();

    }

    //初始化zk的数据
    private void initJobs() {
        List<StandbyNodeData> standbyNodeDataList = standbyApiFactory.nodeApi().getAllNodes();
        if (!ListHelper.isEmpty(standbyNodeDataList)) {
            return;
        }
        List<StandbyJobData> standbyJobDataList = new ArrayList<>();
        try {
            standbyJobDataList = standbyApiFactory.jobApi().getAllJobs(); //获取所有zk的standbyJobData的数据
        } catch (Throwable e) {
            if (e instanceof ESchedulerException) {
                e = e.getCause();
            }
            if (e instanceof KeeperException.NoNodeException) {
                LoggerHelper.info("job path not found. skip init jobs.");
            } else {
                LoggerHelper.warn("get jobs failed. ", e);
            }
        }
        for (StandbyJobData standbyJobData : standbyJobDataList) {
            StandbyJobData.Data data = standbyJobData.getData();
            data.init();
            standbyApiFactory.jobApi().updateJob(data.getGroupName(), data.getJobName(), data);
        }
    }

    //切换leader
    protected synchronized void doJoin() {
        leaderSelector.start();
    }

    protected synchronized void doExit() {
        try {
            if (jobCache != null) {
                jobCache.close();
            }
            LoggerHelper.info("job cache has been closed.");
        } catch (Throwable e) {
            LoggerHelper.error("job cache close failed.", e);
        }
        //关闭quater
        schedulerManager.shutdown();
        LoggerHelper.info("all scheduler has been shutdown.");
        standbyApiFactory.nodeApi().deleteNode(nodePath);//删除节点
        LoggerHelper.info(getNodeIp() + " has been deleted.");
        leaderSelector.close();//leader关闭
        LoggerHelper.info("leaderSelector has been closed.");
        client.close();//关闭客户端
    }

    /**
     * 这个listener是当节点取得Master权限时需要做的事情。我们可以看到，acquireLeadership这个方法中，
     * 主要调用了startupJobs这个方法，然后更新了自己的节点信息。而startupJobs这个方法，
     * 它做的事情就是检查zk节点中现在正在运行的任务，如果发现的话，就把这些任务启动。
     *
     * 这就意味着，当其它节点挂掉的时候，任何一个节点得到Master的权限，都会检查当前正在运行的任务，
     * 并且把它们在自己的节点上启动，这就是standby模式的备份策略。而relinquishLeadership这个方法
     * 则是在节点失去Master权限时要做的事情，而我们做的，就是把它当下所有的任务都关闭。
     */
    private class StandbyLeadershipSelectorListener extends AbstractLeadershipSelectorListener {

        //竞选leader
        @Override
        public void acquireLeadership() throws Exception {
            StandbyNodeData.Data nodeData = new StandbyNodeData.Data(getNodeIp());//节点数据
            int runningJobCount = startupJobs();//启动的job的数量
            nodeData.setRunningJobCount(runningJobCount);
            nodeData.setNodeState("Master");
            standbyApiFactory.nodeApi().updateNode(nodePath, nodeData);//更新主节点的zk节点数据
            LoggerHelper.info(getNodeIp() + " has been updated. [" + nodeData + "]");
            jobCache.start();//开启job的缓存，缓存不会自动，要显示调用
        }

        //检查启动的job的数量
        private Integer startupJobs() {
            List<StandbyJobData> standbyJobDataList = standbyApiFactory.jobApi().getAllJobs();
            int runningJobCount = 0;
            if (ListHelper.isEmpty(standbyJobDataList)) {
                return runningJobCount;
            }
            for (StandbyJobData standbyJobData : standbyJobDataList) {
                try {
                    StandbyJobData.Data data = standbyJobData.getData();
                    if ("Startup".equals(data.getJobState())) {
                        schedulerManager.startupManual(downloadJarFile(data.getJarFileName()), data.getPackagesToScan(), data.isSpring(), data.getGroupName(), data.getJobName(), data.getJobCron(), data.getMisfirePolicy());
                        runningJobCount++;
                    }
                } catch (Exception e) {
                    LoggerHelper.error("start jar failed [" + standbyJobData.getPath() + "]", e);
                }
            }
            return runningJobCount;
        }

        //放弃leader时的操作
        @Override
        public void relinquishLeadership() {
            try {
                if (jobCache != null) {
                    jobCache.close();
                }
                LoggerHelper.info("job cache has been closed.");
            } catch (Throwable e) {
                LoggerHelper.warn("job cache close failed.", e);
            }
            LoggerHelper.info("begin stop scheduler manager.");
            schedulerManager.shutdown();
            if (client.getState() == CuratorFrameworkState.STARTED) {
                StandbyNodeData.Data data = new StandbyNodeData.Data(getNodeIp());
                standbyApiFactory.nodeApi().updateNode(nodePath, data);
                LoggerHelper.info(getNodeIp() + " has been shutdown. [" + data + "]");
            }
            LoggerHelper.info("clear node successfully.");
        }

    }

    /***
     * 首先，这个listener是当Job的zk节点数据有更新时，才会触发。而zk节点数据有更新，
     * 一般都是控制台发来的启动、暂停或者重启的命令。这个时候，我们就需要进行一系列的检查，
     * 比如该节点是否是Master，事件是否是控制台发来的等等。
     *
     * 如果满足条件，就会执行executeOperation方法，这个时候会根据控制台发过来的动作进行任务的调度。
     * 由于我们这个listener只有Master节点才启动（在StandbyLeadershipSelectorListener的11行启动），
     * 因此这就意味着，只有Master节点才会响应控制台的任务调度。
     *
     * 如此一来，我们就能保证只有Master节点才会运行任务，并且集群的节点之间可以容灾。
     * 当Master挂掉时，其它节点会获取到Master权限，这个时候，获得Master权限的节点就会接替任务运行的责任。
     */
    //监听job的状态，启动，关闭等
    private class JobCacheListener implements PathChildrenCacheListener {

        @Override
        public synchronized void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
            AssertHelper.isTrue(isJoined(), "illegal state .");
            boolean hasLeadership = leaderSelector != null && leaderSelector.hasLeadership();
            if (!hasLeadership) {
                return;
            }
            if (!EventHelper.isChildModifyEvent(event)) {
                return;
            }
            StandbyJobData standbyJobData = new StandbyJobData(event.getData());
            if (StringHelper.isEmpty(standbyJobData.getData().getJobOperation())) {
                return;
            }
            StandbyJobData.Data data = standbyJobData.getData();
            if (data.isUnknownOperation()) {
                return;
            }
            StandbyNodeData.Data nodeData = standbyApiFactory.nodeApi().getNode(nodePath).getData();
            executeOperation(nodeData, data);
        }

        //执行操作（启动，重启，暂停）
        private void executeOperation(StandbyNodeData.Data nodeData, StandbyJobData.Data data) {
            try {
                if (data.isStart() || data.isRestart()) {
                    //启动，重启，schedulerManager是quart的调度器封装
                    schedulerManager.startupManual(downloadJarFile(data.getJarFileName()), data.getPackagesToScan(), data.isSpring(), data.getGroupName(), data.getJobName(), data.getJobCron(), data.getMisfirePolicy());
                    if (data.isStart()) {
                        nodeData.increment();
                    }
                    data.setJobState("Startup");
                } else {
                    //暂停
                    schedulerManager.shutdown(data.getGroupName(), data.getJobName());
                    nodeData.decrement();
                    data.setJobState("Pause");
                }
                data.operateSuccess();
                standbyApiFactory.jobApi().updateJob(data.getGroupName(), data.getJobName(), data);
                standbyApiFactory.nodeApi().updateNode(nodePath, nodeData);
            } catch (Throwable e) {
                LoggerHelper.error("handle operation failed. " + data, e);
                data.operateFailed(ExceptionHelper.getStackTrace(e, true));
                standbyApiFactory.jobApi().updateJob(data.getGroupName(), data.getJobName(), data);
            }
        }

    }

}
