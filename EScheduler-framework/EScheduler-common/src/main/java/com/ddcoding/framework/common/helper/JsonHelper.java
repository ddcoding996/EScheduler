package com.ddcoding.framework.common.helper;

import com.google.gson.Gson;

/**
 * 基于GSON的JSON解析器
 *
 */
public abstract class JsonHelper {

    private static final Gson GSON = new Gson();

    public static byte[] toBytes(Object object) {
        return StringHelper.getBytes(toJson(object));
    }

    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        return GSON.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <T> T fromJson(byte[] bytes, Class<T> clazz) {
        return fromJson(StringHelper.getString(bytes), clazz);
    }

}
