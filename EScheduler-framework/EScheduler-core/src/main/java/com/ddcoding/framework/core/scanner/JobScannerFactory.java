package com.ddcoding.framework.core.scanner;


import com.ddcoding.framework.common.helper.StringHelper;

/**
 * job扫描器工厂,负责创建job扫描器
 *
 */
public final class JobScannerFactory {

    private JobScannerFactory() {}

    public static JobScanner createJarFileJobScanner(ClassLoader classLoader, String packagesToScan, String... jarFilePaths) {
        return new LocalAndRemoteJobScanner(classLoader, packagesToScan, false, jarFilePaths);
    }

    public static JobScanner createClasspathJobScanner(ClassLoader classLoader, String packagesToScan) {
        return new LocalAndRemoteJobScanner(classLoader, packagesToScan, true, StringHelper.emptyArray());
    }

}
