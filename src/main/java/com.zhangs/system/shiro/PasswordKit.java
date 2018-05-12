package com.zhangs.system.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jie on 2017/4/7.
 * 密码生成工具,并返回随机加密盐
 */
public class PasswordKit {
    private static final String algorithmName = "md5";
    private static final int hashIterations = 2;
    public static final String ENCRYPT_KEY = "password";
    public static final String ENCRYPT_SALT = "salt";

    /**
     * 使用MD5加密
     *
     * @param password 待加密串
     * @return 加密结果 和 加密盐
     */
    public static Map<String, String> encryptPassword(String password, String salt) {
//        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();//
//        String salt = randomNumberGenerator.nextBytes().toHex(); //随机数盐

        String newPassword = new SimpleHash(
                algorithmName,//加密算法
                password,//待加密串
                ByteSource.Util.bytes(salt),//加密盐
                hashIterations).toHex();
        Map<String, String> encryptMap = new HashMap<String, String>();
        encryptMap.put(ENCRYPT_KEY, newPassword);
        encryptMap.put(ENCRYPT_SALT, salt);
        return encryptMap;
    }
}
