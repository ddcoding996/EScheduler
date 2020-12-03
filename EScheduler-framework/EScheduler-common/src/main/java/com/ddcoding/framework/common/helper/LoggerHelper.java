package com.ddcoding.framework.common.helper;

import org.apache.log4j.Logger;

/**
 * 日志工具类.
 *
 */
public abstract class LoggerHelper {

    public static void info(Class<?> clazz, String message) {
        info(clazz, message, null);
    }

    public static void info(String message) {
        info(null, message, null);
    }

    public static void info(String message, Throwable throwable) {
        info(null, message, throwable);
    }

    public static void info(Class<?> clazz, String message, Throwable throwable) {
        AssertHelper.notNull(message, "message can't be null!");
        if (clazz == null) {
            clazz = LoggerHelper.class;
        }
        Logger logger = Logger.getLogger(clazz);
        if (logger.isInfoEnabled() && throwable == null) {
            logger.info(message);
        }
        if (logger.isInfoEnabled() && throwable != null) {
            logger.info(message, throwable);
        }
    }

    public static void debug(String message) {
        debug(null, message, null);
    }

    public static void debug(String message, Throwable throwable) {
        debug(null, message, throwable);
    }

    public static void debug(Class<?> clazz, String message, Throwable throwable) {
        AssertHelper.notNull(message, "message can't be null!");
        if (clazz == null) {
            clazz = LoggerHelper.class;
        }
        Logger logger = Logger.getLogger(clazz);
        if (logger.isDebugEnabled() && throwable == null) {
            logger.debug(message);
        }
        if (logger.isDebugEnabled() && throwable != null) {
            logger.debug(message, throwable);
        }
    }

    public static void warn(String message) {
        warn(null, message, null);
    }

    public static void warn(String message, Throwable throwable) {
        warn(null, message, throwable);
    }

    public static void warn(Class<?> clazz, String message, Throwable throwable) {
        AssertHelper.notNull(message, "message can't be null!");
        if (clazz == null) {
            clazz = LoggerHelper.class;
        }
        Logger logger = Logger.getLogger(clazz);
        if (throwable == null) {
            logger.warn(message);
        }
        if (throwable != null) {
            logger.warn(message, throwable);
        }
    }

    public static void error(String message) {
        error(null, message, null);
    }

    public static void error(Class<?> clazz, String message) {
        error(clazz, message, null);
    }

    public static void error(String message, Throwable throwable) {
        error(null, message, throwable);
    }

    public static void error(Class<?> clazz, String message, Throwable throwable) {
        if (clazz == null) {
            clazz = LoggerHelper.class;
        }
        AssertHelper.notNull(message, "message can't be null!");
        AssertHelper.notNull(throwable, "throwable can't be null!");
        Logger logger = Logger.getLogger(clazz);
        logger.error(message, throwable);
    }

}
