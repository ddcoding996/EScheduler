
package com.ddcoding.framework.api;

/**
 * 抽象工厂模式中的主从模式工厂接口
 *
 */
public interface MasterSlaveApiFactory {

    MasterSlavePathApi pathApi();

    MasterSlaveNodeApi nodeApi();

    MasterSlaveJobApi jobApi();

}
