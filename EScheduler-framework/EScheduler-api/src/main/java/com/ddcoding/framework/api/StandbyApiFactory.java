
package com.ddcoding.framework.api;

/**
 * 抽象工厂模式中的主备模式工厂接口
 *
 */
public interface StandbyApiFactory {

    StandbyPathApi pathApi();

    StandbyNodeApi nodeApi();

    StandbyJobApi jobApi();

}
