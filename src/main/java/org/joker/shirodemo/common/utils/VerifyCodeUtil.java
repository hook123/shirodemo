package org.joker.shirodemo.common.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

/**
 * @Author joker
 * @Date 3/25/19 5:56 PM
 * @Description
 */
public class VerifyCodeUtil {

    //字体只显示大写，去掉了1,0,i,o几个容易混淆的字符
    public static final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    //验证码的Key
    public static final String V_CODE = "_CODE";

    /**
     * 验证码对象
     *
     */
    @Getter@Setter
    public static class Verify{

        private String code;//如 1 + 2

        private Integer value;//如  3
    }

    public static Verify generateVerify(){
        int number1 = new Random().nextInt(10) + 1;;
        int number2 = new Random().nextInt(10) + 1;;
        Verify entity = new Verify();
        entity.setCode(number1  + " x " + number2);
        entity.setValue(number1 + number2);
        return entity;
    }

}
