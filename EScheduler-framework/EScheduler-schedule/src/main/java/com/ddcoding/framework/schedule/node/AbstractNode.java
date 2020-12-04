
package com.ddcoding.framework.schedule.node;

import com.ddcoding.framework.common.exception.ESchedulerException;
import com.ddcoding.framework.common.helper.ClassHelper;
import com.ddcoding.framework.common.helper.LoggerHelper;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * 抽象的任务执行节点.
 * 该类封装了配置加载和IP初始化的过程.
 *
 */
public abstract class AbstractNode implements Node {

    private String nodeIp;

    /**
     * 用于非集群环境
     */
    public AbstractNode() {
        Properties properties = new Properties();
        try {
            properties.load(ClassHelper.getDefaultClassLoader().getResourceAsStream("log4j.properties"));
        } catch (IOException e) {
            throw new ESchedulerException(e);
        }
        try {
            properties.load(ClassHelper.getDefaultClassLoader().getResourceAsStream("log4j.properties"));
        } catch (Exception e) {
            LoggerHelper.warn("log4j properties not found ,use default instead.");
        }
        PropertyConfigurator.configure(properties);
        try {
            this.nodeIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            this.nodeIp = "unknown";
        }
    }
    /**
     * 用于集群环境.集群环境下需要传入自己的配置信息
     *
     * @param properties
     */
    public AbstractNode(Properties properties) {
        PropertyConfigurator.configure(properties);
        try {
            this.nodeIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            this.nodeIp = "unknown";
        }
    }

    protected String getNodeIp() {
        return nodeIp;
    }

}
