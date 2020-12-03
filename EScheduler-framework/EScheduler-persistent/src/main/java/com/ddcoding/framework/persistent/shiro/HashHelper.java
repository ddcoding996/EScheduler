
package com.ddcoding.framework.persistent.shiro;

import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 */
public abstract class HashHelper {

    private static final String hashAlgorithm = "MD5";

    public static String getHashedPassword(String password, String salt) {
        Hash hash = new SimpleHash(hashAlgorithm, password, salt);
        return hash.toHex();
    }

}
