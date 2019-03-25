package org.joker.shirodemo.common.utils.vcode;

import java.awt.*;
import java.io.OutputStream;
import java.util.Random;

/**
 * @Author joker
 * @Date 3/25/19 7:11 PM
 * @Description 验证码抽象类
 */
public abstract class Captcha {

    private static final Random RANDOM = new Random();
    //定义验证码字符.去除了O和I等容易混淆的字母
    public static final char SIMPLE_CODE[]={'A','B','C','D','E','F','G','H','G','K','M','N','P','Q','R','S','T','U','V','W','X','Y','Z'
            ,'a','b','c','d','e','f','g','h','i','j','k','m','n','p','q','r','s','t','u','v','w','x','y','z','2','3','4','5','6','7','8','9'};


    protected Font font = new Font("Verdana", Font.ITALIC | Font.BOLD, 25);  //验证码中使用字体
    protected int len = 4; // 验证码随机字符长度
    protected int width = 150; // 验证码显示跨度
    protected int height = 40; // 验证码显示高度
    private String randomChars = null; // 随机字符串

    //get,set方法--------------------------
    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    //------------------------------------


    /**
     * 生成随机字符数组
     *
     * @return 字符数组
     */
    protected char[] randomChars() {
        char[] cs = new char[len];
        for (int i = 0; i < len; i++) {
            cs[i] = getOneChar();
        }
        randomChars = new String(cs);
        return cs;
    }


    /**
     * 给定范围获得随机颜色
     *
     * @return Color 随机颜色
     */
    protected Color color(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + num(bc - fc);
        int g = fc + num(bc - fc);
        int b = fc + num(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 验证码输出,抽象方法，由子类实现
     *
     * @param os
     *            输出流
     */
    public abstract void out(OutputStream os);


    /**
     * 获取随机字符串
     *
     * @return string
     */
    public String getRandomChars() {
        return randomChars;
    }



    /**
     * 产生两个数之间的随机数
     * @param min 小数
     * @param max 比min大的数
     * @return int 随机数字
     */
    public static int num(int min, int max)
    {
        return min + RANDOM.nextInt(max - min);
    }

    /**
     * 产生0--num的随机数,不包括num
     * @param num 数字
     * @return int 随机数字
     */
    public static int num(int num)
    {
        return RANDOM.nextInt(num);
    }

    /**
     * 取得验证码样本中的一个字符
     * @return
     */
    public static char getOneChar()
    {
        return SIMPLE_CODE[num(0, SIMPLE_CODE.length)];
    }
}
