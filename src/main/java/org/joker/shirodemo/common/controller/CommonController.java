package org.joker.shirodemo.common.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.joker.shirodemo.common.utils.vcode.Captcha;
import org.joker.shirodemo.common.utils.vcode.GifCaptcha;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author joker
 * @Date 3/25/19 8:16 PM
 * @Description
 */
@RestController
public class CommonController {


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("redirect:/static/index.html");
    }


    @RequestMapping(value = "/getGifCode", method = RequestMethod.GET)
    public void getGifCode(HttpServletResponse response) {

        Session session = SecurityUtils.getSubject().getSession();//必须放在最前面，否则session设置不上
        response.setHeader("Pragma", "No-cache");//客户端不缓存，兼容http 1.0
        response.setHeader("Cache-Control", "no-cache");//客户端不缓存
        response.setDateHeader("Expires", 0);//立即过期
        response.setContentType("image/gif");//文件类型

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            Captcha captcha = new GifCaptcha(100, 30);
            captcha.out(out);
            out.flush();
            String vCode = captcha.getRandomChars().toLowerCase();
            session.setAttribute("vcode", vCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
