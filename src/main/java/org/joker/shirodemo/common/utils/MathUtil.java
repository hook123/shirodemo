package org.joker.shirodemo.common.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @Author joker
 * @Date 3/24/19 5:53 PM
 * @Description
 */
public class MathUtil {

    public static String getMD5(String password,String username){
        SimpleHash md5 = new SimpleHash("MD5", password, username, 1024);
        return md5.toString();
    }
}
