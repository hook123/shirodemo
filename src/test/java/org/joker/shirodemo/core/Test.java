package org.joker.shirodemo.core;

import org.joker.shirodemo.common.utils.MathUtil;

/**
 * @Author joker
 * @Date 3/24/19 6:42 PM
 * @Description
 */
public class Test {

    @org.junit.Test
    public void MD5Test(){

        String password = MathUtil.getMD5("root", "root");
        System.out.println(password);
    }
}
