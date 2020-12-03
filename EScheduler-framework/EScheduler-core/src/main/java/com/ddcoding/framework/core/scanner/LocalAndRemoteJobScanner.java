package com.ddcoding.framework.core.scanner;

import com.ddcoding.framework.common.exception.ConfigException;
import com.ddcoding.framework.common.helper.ClassHelper;
import com.ddcoding.framework.common.helper.LoggerHelper;
import com.ddcoding.framework.common.helper.StringHelper;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 唯一的job扫描器实现.
 * 该实现同时实现了扫描classpath和jar包的功能
 *
 */
public class LocalAndRemoteJobScanner extends AbstractJobScanner {

    /**
     * 扫描器唯一的构造方法,将使用指定的类加载器去扫描
     *
     * @param classLoader 扫描器所使用的类加载器
     * @param packagesToScan 将被扫描的package
     * @param containsClasspath 是否要扫描classpath路径下的类
     * @param jarFilePaths 需要扫描的jar包
     */
    LocalAndRemoteJobScanner(ClassLoader classLoader, String packagesToScan, boolean containsClasspath, String... jarFilePaths) {
        super(classLoader, packagesToScan, jarFilePaths);
        if (containsClasspath) {
            scanClasspath();
        }
        scanJarFiles();
    }

    protected void scanClasspath() {
        URL url = getClassLoader().getResource("");
        if (url == null) {
            LoggerHelper.error("classpath can't be find.");
            throw new ConfigException();
        }
        if (url.getProtocol().toLowerCase().equals("file")) {
            LoggerHelper.info("scan classpath [" + url + "]");
            File[] children = new File(url.getFile()).listFiles();
            if (children != null && children.length > 0) {
                for (File child : children) {
                    fill("", child);
                }
            }
        } else {
            LoggerHelper.warn("url [" + url + "] is not a file but a " + url.getProtocol() + ".");
        }
    }

    protected void scanJarFiles() {
        for (String jarFilePath : getJarFilePaths()) {
            File file = new File(jarFilePath);
            if (file.exists()) {
                scanJarFile(jarFilePath);
            } else {
                LoggerHelper.warn("jar file [" + jarFilePath + "] can't be found.");
            }
        }
    }

    private void fill(String packageName, File file) {
        String fileName = file.getName();
        if (file.isFile() && fileName.endsWith(".class")) {
            String className = packageName + "." + fileName.substring(0, fileName.lastIndexOf("."));
            super.scanClass(className);
        } else if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null && children.length > 0) {
                for (File child : children) {
                    if (StringHelper.isEmpty(packageName)) {
                        fill(fileName, child);
                    } else {
                        fill(packageName + "." + fileName, child);
                    }
                }
            }
        }
    }

    private void scanJarFile(String jarFilePath) {
        JarFile jarFile;
        try {
            jarFile = new JarFile(jarFilePath);
        } catch (Throwable e) {
            LoggerHelper.warn("get jar file failed. [" + jarFilePath +"]");
            return;
        }
        Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
        while (jarEntryEnumeration.hasMoreElements()) {
            String jarEntryName = jarEntryEnumeration.nextElement().getName();
            if (jarEntryName != null && jarEntryName.equals(APPLICATION_CONTEXT_XML_PATH)) {
                setHasSpringEnvironment(true);
                LoggerHelper.info("find spring config file [" + APPLICATION_CONTEXT_XML_PATH + "] .");
                continue;
            }
            if (jarEntryName == null || !jarEntryName.endsWith(".class")) {
                continue;
            }
            String className = ClassHelper.getClassName(jarEntryName);
            super.scanClass(className);
        }
    }

}
