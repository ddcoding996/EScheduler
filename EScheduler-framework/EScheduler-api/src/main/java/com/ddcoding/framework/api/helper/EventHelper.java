package com.ddcoding.framework.api.helper;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

/**
 * 事件处理帮助类
 *
 */
public interface EventHelper {

    static boolean isChildAddEvent(PathChildrenCacheEvent event) {
        return event != null && event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED;
    }

    static boolean isChildUpdateEvent(PathChildrenCacheEvent event) {
        return event != null && event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED;
    }

    static boolean isChildRemoveEvent(PathChildrenCacheEvent event) {
        return event != null && event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED;
    }

    static boolean isChildModifyEvent(PathChildrenCacheEvent event) {
        return isChildAddEvent(event) || isChildRemoveEvent(event) || isChildUpdateEvent(event);
    }

}
