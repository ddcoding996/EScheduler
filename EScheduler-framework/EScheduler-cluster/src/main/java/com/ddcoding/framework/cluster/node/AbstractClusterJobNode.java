package com.ddcoding.framework.cluster.node;

import com.ddcoding.framework.cluster.startup.Bootstrap;
import com.ddcoding.framework.common.exception.ESchedulerException;
import com.ddcoding.framework.common.helper.AssertHelper;
import com.ddcoding.framework.common.helper.JarFileHelper;
import com.ddcoding.framework.common.helper.LoggerHelper;
import com.ddcoding.framework.schedule.DefaultManualScheduleManager;
import com.ddcoding.framework.schedule.ManualScheduleManager;
import com.ddcoding.framework.schedule.node.AbstractNode;
import com.ddcoding.framework.schedule.node.Node;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 集群Job节点抽象类,封装了节点状态的切换,子类只需要实现自己加入和退出集群的方法.
 * 典型的模板方法模式
 *
 *
 * @see MasterSlaveNode
 * @see StandbyNode
 */
public abstract class AbstractClusterJobNode extends AbstractNode implements Node {

    protected AtomicReference<State> state;

    protected ManualScheduleManager schedulerManager;

    public AbstractClusterJobNode() {
        super(Bootstrap.properties());
        this.schedulerManager = new DefaultManualScheduleManager(Bootstrap.properties());
        this.state = new AtomicReference<>();
        this.state.set(State.LATENT);
    }

    protected enum State { LATENT, JOINED, EXITED}

    protected boolean isJoined() {
        return this.state.get() == State.JOINED;
    }

    protected String downloadJarFile(String jarFileName) {
        String jarFilePath;
        try {
            jarFilePath = JarFileHelper.downloadJarFile(Bootstrap.getJobDir(), Bootstrap.getJarUrl(jarFileName));
        } catch (IOException e) {
            LoggerHelper.error("download jar file failed. [" + jarFileName + "]", e);
            throw new ESchedulerException(e);
        }
        return jarFilePath;
    }

    @Override
    public void join() {
        AssertHelper.isTrue(state.compareAndSet(State.LATENT, State.JOINED), "illegal state .");
        doJoin();
    }

    @Override
    public void exit() {
        AssertHelper.isTrue(state.compareAndSet(State.JOINED, State.EXITED), "illegal state .");
        doExit();
    }

    protected abstract void doJoin();

    protected abstract void doExit();

    /**
     * Master节点抽象监听器.
     * 本抽象类封装了获取Master权限和失去Master权限时线程的挂起控制
     */
    protected abstract class AbstractLeadershipSelectorListener implements LeaderSelectorListener {

        private final AtomicInteger leaderCount = new AtomicInteger();

        private Object mutex = new Object();

        public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
            LoggerHelper.info(getNodeIp() + " is now the leader ,and has been leader " + this.leaderCount.getAndIncrement() + " time(s) before.");
            boolean isJoined = isJoined();
            try {
                if (isJoined) {
                    acquireLeadership();
                }
            } catch (Throwable e) {
                relinquishLeadership();
                LoggerHelper.warn(getNodeIp() + " startup failed,relinquish leadership.", e);
                return;
            }
            try {
                synchronized (mutex) {
                    mutex.wait();
                }
            } catch (InterruptedException e) {
                LoggerHelper.info(getNodeIp() + " has been interrupted.");
            }
        }

        public void stateChanged(CuratorFramework client, ConnectionState newState) {
            LoggerHelper.info(getNodeIp() + " state has been changed [" + newState + "]");
            if (!newState.isConnected()) {
                relinquishLeadership();
                synchronized (mutex) {
                    mutex.notify();
                }
            }
        }

        //竞选leader
        public abstract void acquireLeadership() throws Exception;

        //relinquish 放弃，即放弃leader选举
        public abstract void relinquishLeadership();

    }

}
