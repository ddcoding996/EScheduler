
package com.ddcoding.framework.service.spring;

import com.ddcoding.framework.common.helper.AssertHelper;
import lombok.Setter;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * ZK client的工厂Bean.用于在spring容器中构造一个ZK client.
 *
 */
public class CuratorFrameworkFactoryBean implements FactoryBean<CuratorFramework>, InitializingBean {

    private static final int DEFAULT_SESSION_TIMEOUT_MS = Integer.getInteger("curator-default-session-timeout", 60 * 1000);

    private static final int DEFAULT_CONNECTION_TIMEOUT_MS = Integer.getInteger("curator-default-connection-timeout", 15 * 1000);

    private static final RetryPolicy DEFAULT_RETRY_POLICY = new ExponentialBackoffRetry(1000, Integer.MAX_VALUE);

    @Setter
    private String connectString;

    @Setter
    private Integer sessionTimeoutMs = DEFAULT_SESSION_TIMEOUT_MS;

    @Setter
    private Integer connectionTimeoutMs = DEFAULT_CONNECTION_TIMEOUT_MS;

    @Setter
    private RetryPolicy retryPolicy = DEFAULT_RETRY_POLICY;

    private CuratorFramework client;

    @Override
    public CuratorFramework getObject() throws Exception {
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AssertHelper.notNull(connectString, "connectString property must be set in config.properties.");
        client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        client.start();
    }

}
