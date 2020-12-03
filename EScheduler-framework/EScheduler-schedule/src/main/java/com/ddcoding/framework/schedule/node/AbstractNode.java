package com.ddcoding.framework.schedule.node;

import java.io.IOException;
import java.util.Properties;

/**
 * @Desc
 * @Date 2020/12/2 16:14
 * @Author ddcoding
 */
public abstract class AbstractNode implements Node {
    private String nodeIp;

    /**
     * 集群模式下执行
     */
    public AbstractNode(){
        Properties properties=new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("scheduler/quartz-default.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }
}
