package org.joker.shirodemo.core;

import com.sun.crypto.provider.AESKeyGenerator;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.joker.shirodemo.common.utils.MathUtil;
import org.junit.Assert;

import java.security.Key;


/**
 * @Author joker
 * @Date 3/24/19 6:42 PM
 * @Description
 */
public class Test {

    @org.junit.Test
    public void MD5Test() {

        String password = MathUtil.getMD5("root", "root");
        System.out.println(password);
    }

    @org.junit.Test
    public void Base64Test() {

        String s1 = Base64.encodeToString("root".getBytes());
        System.out.println(s1);
        String s = Base64.decodeToString(s1);
        System.out.println(s1 + "<========>" + s);

    }

    @org.junit.Test
    public void HexTest() {
        String root = Hex.encodeToString("root".getBytes());
        System.out.println(root);
        String s1 = new String(Hex.decode(root));
        System.out.println(root + "<======>" + s1);
    }

    //jQiTNm8lsHQqQPbhnLd8fA==
    @org.junit.Test
    public void AESTest() {
        AesCipherService aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128);
        Key key = aesCipherService.generateNewKey();
        String s1 = Base64.encodeToString(key.getEncoded());
        System.out.println(s1);
        //生成符合要求的秘钥,随机生成
        s1="jQiTNm8lsHQqQPbhnLd8fA==";
        String text = "hello";
        //加密
        String encrptText = aesCipherService.encrypt(text.getBytes(),s1.getBytes()).toBase64();
        System.out.println(encrptText);
        //解密
        String text2 = new String(aesCipherService.decrypt(Base64.decode(encrptText),s1.getBytes()).getBytes());
        System.out.println(text2);
        Assert.assertEquals(text, text2);
    }
}
