package com.ddcoding.framework.service.spring;

import com.ddcoding.framework.api.StandbyApiFactory;
import com.ddcoding.framework.api.curator.StandbyApiFactoryImpl;
import com.ddcoding.framework.api.data.StandbyJobData;
import com.ddcoding.framework.api.helper.EventHelper;
import com.ddcoding.framework.common.helper.LoggerHelper;
import com.ddcoding.framework.service.StandbyJobLogService;
import com.ddcoding.framework.service.StandbyJobSummaryService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 该监听器用于启动一个cache去监听主备集群任务节点数据的变化,并且同步到console数据库
 *
 */
@Component
public class StandbyJobSummaryListener {

    @Autowired
    private StandbyJobSummaryService standbyJobSummaryService;

    @Autowired
    private StandbyJobLogService standbyJobLogService;

    @Autowired
    private CuratorFramework client;

    @PostConstruct
    public void listen() throws Exception {
        StandbyApiFactory standbyApiFactory = new StandbyApiFactoryImpl(client);
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, standbyApiFactory.pathApi().getJobPath(), true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public synchronized void childEvent(CuratorFramework clientInner, PathChildrenCacheEvent event) throws Exception {
                if (!EventHelper.isChildUpdateEvent(event) && !EventHelper.isChildAddEvent(event)) {
                    return;
                }
                StandbyJobData standbyJobData = new StandbyJobData(event.getData());
                if (!standbyJobData.getData().isOperated()) {
                    return;
                }
                LoggerHelper.info("begin update standby job summary " + standbyJobData.getData());
                standbyJobSummaryService.updateJobSummary(standbyJobData.getData());
                standbyJobLogService.updateJobLog(standbyJobData.getData());
                LoggerHelper.info("update standby job summary successfully " + standbyJobData.getData());
            }
        });
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
    }

}
