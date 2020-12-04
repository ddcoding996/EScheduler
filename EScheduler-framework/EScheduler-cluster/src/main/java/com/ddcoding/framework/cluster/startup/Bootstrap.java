
package com.ddcoding.framework.cluster.startup;


import com.ddcoding.framework.common.exception.ConfigException;
import com.ddcoding.framework.common.exception.ESchedulerException;
import com.ddcoding.framework.common.helper.*;
import com.ddcoding.framework.core.scanner.ApplicationClassLoader;
import com.ddcoding.framework.core.scanner.ApplicationClassLoaderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 节点的启动，如何和控制台console结合起来（如何加入集群）？？
 * 节点的启动类,包含Main函数
 *
 */
public final class Bootstrap {

    private Bootstrap() {}

    private static final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

    private static final ApplicationClassLoader applicationClassLoader;

    private static String rootDir;

    private static final String confDir;

    private static final String libDir;

    private static final String jobDir;

    private static final Properties properties;

    private static Object nodeInstance;

    public static void main(String[] args) throws Exception {
        if (!ListHelper.isEmpty(args) && "start".equals(args[0])) {
            start();
            LoggerHelper.info("bootstrap start successfully.");
            await();
            LoggerHelper.info("bootstrap begin stop.");
            stop();
            LoggerHelper.info("bootstrap stop successfully.");
            System.exit(0);
        } else if (!ListHelper.isEmpty(args) && "stop".equals(args[0])) {
            sendCommand("Shutdown");
        }
    }

    //输出到客户端
    public static void sendCommand(String command) throws IOException {
        Socket socket = new Socket("localhost", getShutdownPort());
        socket.getOutputStream().write(StringHelper.getBytes(command));
        socket.getOutputStream().flush();
        socket.close();
    }

    public static void await() throws IOException {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(getShutdownPort(), 1, InetAddress.getByName("localhost"));
        } catch (Exception e) {
            LoggerHelper.error("socket create failed.", e);
            try {
                stop();
            } catch (Exception e1) {
                LoggerHelper.error("stop node failed.", e1);
                throw new ESchedulerException(e1);
            }
            throw new ESchedulerException(e);
        }
        //在此阻塞
        while (true) {
            Socket socket = serverSocket.accept();//accept()阻塞
            String command = StringHelper.getString(IOHelper.readStreamBytes(socket.getInputStream()));
            if ("Shutdown".equals(command)) {
                break;
            }
        }
    }

    static {
        String userDir = System.getProperty("user.dir").replace("\\", "/");
        //用于测试环境
        rootDir = userDir + "/target";
        File binDir = new File(rootDir + "/bin");
        if (!binDir.exists()) {
            //用于生产环境
            rootDir = userDir.substring(0, userDir.lastIndexOf("/"));
            binDir = new File(rootDir + "/bin");
            if (!binDir.exists()) {
                throw new ESchedulerException(new IllegalArgumentException("can't find bin path."));
            }
        }
        confDir = rootDir + "/conf";
        libDir = rootDir + "/lib";
        jobDir = rootDir + "/job";

        ApplicationClassLoaderFactory.setSystemClassLoader(systemClassLoader);

        applicationClassLoader = ApplicationClassLoaderFactory.getNodeApplicationClassLoader();

        Thread.currentThread().setContextClassLoader(applicationClassLoader);

        initApplicationClassLoader();

        properties = new Properties();

        loadProperties();
    }

    //初始化加载器的classpath，即加载所有jar
    private static void initApplicationClassLoader() {
        File libFile = new File(libDir);
        if (!libFile.exists()) {
            throw new ESchedulerException(new IllegalArgumentException("can't find lib path. [" + libDir + "]"));
        }
        List<String> filePathList = new ArrayList<>();

        File[] jarFiles = libFile.listFiles();
        for (File jarFile : jarFiles) {
            if (jarFile.getName().endsWith(".jar")) {
                filePathList.add(jarFile.getAbsolutePath());
            }
        }
        applicationClassLoader.addFiles(filePathList.toArray());
    }
    //加载配置文件
    private static void loadProperties() {
        try {
            File confFile = new File(confDir);
            if (!confFile.exists()) {
                throw new ESchedulerException(new IllegalArgumentException("can't find conf path."));
            }
            File[] propertiesFiles = confFile.listFiles();
            for (File propertiesFile : propertiesFiles) {
                if (propertiesFile.getName().endsWith(".properties")) {
                    properties.load(new FileInputStream(propertiesFile));
                }
            }
        } catch (IOException e) {
            throw new ESchedulerException(e);
        }
    }

    public static String getJarRepertoryUrl() {
        return StringHelper.appendSlant(properties.getProperty("jar.repertory.url", "http://localhost:8080")) + "job/";
    }

    public static Integer getShutdownPort() {
        return Integer.valueOf(properties.getProperty("shutdown.port", "9101"));
    }

    public static String getNodeMode() {
        return properties.getProperty("node.mode","masterSlave");
    }

    public static String getZookeeperAddresses() {
        return properties.getProperty("zookeeper.addresses","localhost:2181");
    }

    public static Properties properties() {
        return new Properties(properties);
    }

    public static String getJarUrl(String jarFileName) {
        if ("masterSlave".equals(getNodeMode())) {
            return getJarRepertoryUrl() + "masterSlave/" + jarFileName;
        } else if ("standby".equals(getNodeMode())) {
            return getJarRepertoryUrl() + "standby/" + jarFileName;
        } else {
            throw new ConfigException();
        }
    }

    public static String getJobDir() {
        if ("masterSlave".equals(getNodeMode())) {
            return jobDir + "/masterSlave";
        } else if ("standby".equals(getNodeMode())) {
            return jobDir + "/standby";
        } else {
            throw new ConfigException();
        }
    }

    /**
     * 这里为什么不直接new出来对象，然后调用start()即可？？
     * 作者为什么这么写？
     * 答案：1，因为启动的模式由用户在配置设定，所以你不能确定new哪一个类对象；
     *      2.两个都new出来显然系统冲突，只能new一个，所以反射一个即可
     * @throws Exception
     */
    public static void start() throws Exception {
        String nodeClassName;
        if ("masterSlave".equals(getNodeMode())) {
            nodeClassName = "com.ddcoding.framework.cluster.node.MasterSlaveNode";
        } else if ("standby".equals(getNodeMode())) {
            nodeClassName = "com.ddcoding.framework.cluster.node.StandbyNode";
        } else {
            throw new ConfigException();
        }
        Class<?> nodeClass = applicationClassLoader.loadClass(nodeClassName);
        Constructor<?> nodeConstructor = nodeClass.getConstructor();
        nodeInstance = nodeConstructor.newInstance();
        //反射调用节点启用的join方法，加入集群
        Method joinMethod = ReflectHelper.getInheritMethod(nodeClass, "join");
        joinMethod.invoke(nodeInstance);
    }
    /**
     * 这里为什么不直接new出来对象，然后调用start()即可？？
     * 作者为什么这么写？
     * @throws Exception
     */
    public static void stop() throws Exception {
        String nodeClassName;
        if ("masterSlave".equals(getNodeMode())) {
            nodeClassName = "com.ddcoding.framework.cluster.node.MasterSlaveNode";
        } else if ("standby".equals(getNodeMode())) {
            nodeClassName = "com.ddcoding.framework.cluster.node.StandbyNode";
        } else {
            throw new ConfigException();
        }
        if (nodeInstance != null ) {
            Class<?> nodeClass = applicationClassLoader.loadClass(nodeClassName);
            Method exitMethod = ReflectHelper.getInheritMethod(nodeClass, "exit");
            exitMethod.invoke(nodeInstance);
        }
    }

}
