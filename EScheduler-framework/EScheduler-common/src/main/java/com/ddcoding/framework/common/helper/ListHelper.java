package com.ddcoding.framework.common.helper;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 列表帮助类
 *
 */
public interface ListHelper {

    static <T> List<T> add(List<T> list, T element) {
        List<T> mergeResult = new ArrayList<>();
        if (list != null) {
            mergeResult.addAll(list);
        }
        if (element != null) {
            mergeResult.add(element);
        }
        return mergeResult;
    }

    static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

}
