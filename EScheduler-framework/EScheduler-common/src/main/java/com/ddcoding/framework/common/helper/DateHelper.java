
package com.ddcoding.framework.common.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期帮助类.
 *
 */
public interface DateHelper {

    /**
     * 格式化日期为标准的日期字符串,{@code "yyyy-MM-dd HH:mm:ss"}
     *
     * @param date 日期
     * @return 格式化后的日期字符串
     */
    static String format(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

}
