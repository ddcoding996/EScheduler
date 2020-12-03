package com.ddcoding.framework.common.helper;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * IO帮助类
 *
 */
public interface IOHelper {


    /**
     * 将数据写入文件
     *
     * @param fileName 文件名称
     * @param bytes 数据
     * @throws IOException
     */
    static void writeFile(String fileName, byte[] bytes) throws IOException {
        if (fileName != null) {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }

    /**
     * 读取输入流中的字节
     *
     * @param inputStream 输入流
     * @return 读出来的字节数组
     * @throws IOException
     */
    static byte[] readStreamBytes(InputStream inputStream) throws IOException {
        byte[] cache = new byte[2048];
        int len;
        byte[] bytes = new byte[0];
        while ((len = inputStream.read(cache)) > 0) {
            byte[] temp = bytes;
            bytes = new byte[bytes.length + len];
            System.arraycopy(temp, 0, bytes, 0, temp.length);
            System.arraycopy(cache, 0, bytes, temp.length, len);
        }
        if (bytes.length == 0) {
            return null;
        }
        return bytes;
    }

    static byte[] readStreamBytesAndClose(InputStream inputStream) throws IOException {
        byte[] bytes = readStreamBytes(inputStream);
        inputStream.close();
        return bytes;
    }

}
