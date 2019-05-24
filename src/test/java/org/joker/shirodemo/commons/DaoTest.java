package org.joker.shirodemo.commons;

import org.joker.shirodemo.common.model.UPermission;
import org.junit.Test;

/**
 * @Author joker
 * @Date 3/22/19 5:12 PM
 * @Description
 */
public class DaoTest {
    @Test
    public void  toStringTest(){
        UPermission uPermission = new UPermission();
        uPermission.setId(123L);
        uPermission.setUrl("http://www.baidu.com");
        uPermission.setName("123");
        System.out.println(uPermission.toString());
    }
}
