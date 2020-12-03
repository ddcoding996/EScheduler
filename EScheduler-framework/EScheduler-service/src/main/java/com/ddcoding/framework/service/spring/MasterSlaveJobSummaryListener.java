package com.ddcoding.framework.service.spring;

import com.ddcoding.framework.api.MasterSlaveApiFactory;
import com.ddcoding.framework.api.curator.MasterSlaveApiFactoryImpl;
import com.ddcoding.framework.api.data.MasterSlaveJobData;
import com.ddcoding.framework.api.helper.EventHelper;
import com.ddcoding.framework.common.helper.LoggerHelper;
import com.ddcoding.framework.service.MasterSlaveJobLogService;
import com.ddcoding.framework.service.MasterSlaveJobSummaryService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 该监听器用于启动一个cache去监听主从集群任务节点数据的变化,并且同步到console数据库
 *
 */
@Component
public class MasterSlaveJobSummaryListener {

    @Autowired
    private MasterSlaveJobSummaryService masterSlaveJobSummaryService;

    @Autowired
    private MasterSlaveJobLogService masterSlaveJobLogService;

    @Autowired
    private CuratorFramework client;

    @PostConstruct
    public void listen() throws Exception {
        MasterSlaveApiFactory masterSlaveApiFactory = new MasterSlaveApiFactoryImpl(client);
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, masterSlaveApiFactory.pathApi().getJobPath(), true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public synchronized void childEvent(CuratorFramework clientInner, PathChildrenCacheEvent event) throws Exception {
                if (!EventHelper.isChildUpdateEvent(event) && !EventHelper.isChildAddEvent(event)) {
                    return;
                }
                MasterSlaveJobData masterSlaveJobData = new MasterSlaveJobData(event.getData());
                if (!masterSlaveJobData.getData().isOperated()) {
                    return;
                }
                LoggerHelper.info("begin update master-slave job summary " + masterSlaveJobData.getData());
                masterSlaveJobSummaryService.updateJobSummary(masterSlaveJobData.getData());
                masterSlaveJobLogService.updateJobLog(masterSlaveJobData.getData());
                LoggerHelper.info("update master-slave job summary successfully " + masterSlaveJobData.getData());
            }
        });
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
    }

}
