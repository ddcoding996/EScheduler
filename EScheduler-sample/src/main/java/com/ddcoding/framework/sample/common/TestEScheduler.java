package com.ddcoding.framework.sample.common;


import com.ddcoding.framework.schedule.node.Node;
import com.ddcoding.framework.schedule.node.SimpleLocalJobNode;

/**
 * 测试类
 */
public class TestEScheduler {

    public static void main(String[] args) {
        //com.ddcoding.framework为任务所在的包，这个参数指定了Escheduler需要扫描哪些包找到任务。
        Node node = new SimpleLocalJobNode("com.ddcoding.framework");
        node.join();
    }

}
