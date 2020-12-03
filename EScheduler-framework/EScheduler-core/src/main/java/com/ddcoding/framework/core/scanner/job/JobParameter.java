package com.ddcoding.framework.core.scanner.job;


import java.util.HashMap;

/**
 * 任务参数,用于支持使用参数启动任务.
 *
 */
public class JobParameter extends HashMap<String, Object> {

    public Integer getInteger(String key) {
        return Integer.valueOf(get(key).toString());
    }

    public Long getLong(String key) {
        return Long.valueOf(get(key).toString());
    }

    public String getString(String key) {
        return get(key).toString();
    }

}
