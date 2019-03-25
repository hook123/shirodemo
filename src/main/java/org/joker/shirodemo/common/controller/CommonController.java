package org.joker.shirodemo.common.controller;


import org.joker.shirodemo.common.utils.vcode.Captcha;
import org.joker.shirodemo.common.utils.vcode.GifCaptcha;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author joker
 * @Date 3/25/19 8:16 PM
 * @Description
 */
@RestController
@RequestMapping(value = "/common")
public class CommonController {

    @RequestMapping(value = "/getGifCode.do",method = RequestMethod.GET)
    public void getGifCode(HttpServletResponse response){

        response.setHeader("Pragma", "No-cache");//客户端不缓存，兼容http 1.0
        response.setHeader("Cache-Control", "no-cache");//客户端不缓存
        response.setDateHeader("Expires", 0);//立即过期
        response.setContentType("image/gif");//文件类型

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            Captcha captcha = new GifCaptcha(100,30);
            captcha.out(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
