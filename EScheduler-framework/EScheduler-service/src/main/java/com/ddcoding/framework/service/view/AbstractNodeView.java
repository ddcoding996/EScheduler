package com.ddcoding.framework.service.view;

/**
 * 抽象的节点数据视图类,用于在WEB页面展示
 *
 */
public class AbstractNodeView {

    private String id;

    private String nodeIp;

    private String nodeState;

    private Integer runningJobCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}