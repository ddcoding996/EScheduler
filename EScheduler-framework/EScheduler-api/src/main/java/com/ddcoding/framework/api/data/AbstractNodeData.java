package com.ddcoding.framework.api.data;

/**
 * ZK Node数据节点抽象类
 *
 */
public class AbstractNodeData<T extends AbstractNodeData> implements Comparable<T> {

    private String nodeIp;

    private String nodeState;

    private Integer runningJobCount = 0;

    public AbstractNodeData() {
    }

    public AbstractNodeData(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public String getNodeState() {
        return nodeState;
    }

    public void setNodeState(String nodeState) {
        this.nodeState = nodeState;
    }

    public Integer getRunningJobCount() {
        return runningJobCount;
    }

    public void setRunningJobCount(Integer runningJobCount) {
        this.runningJobCount = runningJobCount;
    }

    public void increment() {
        runningJobCount++;
    }

    public void decrement() {
        runningJobCount--;
    }

    @Override
    public int compareTo(AbstractNodeData data) {
        return this.runningJobCount - data.getRunningJobCount();
    }

    @Override
    public String toString() {
        return "Data{" +
                "nodeIp='" + nodeIp + '\'' +
                ", nodeState='" + nodeState + '\'' +
                ", runningJobCount=" + runningJobCount +
                '}';
    }

}
