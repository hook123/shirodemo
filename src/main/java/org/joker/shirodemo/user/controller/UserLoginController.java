package org.joker.shirodemo.user.controller;


import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.joker.shirodemo.common.model.UUser;
import org.joker.shirodemo.common.utils.LoggerUtils;
import org.joker.shirodemo.common.utils.MathUtil;
import org.joker.shirodemo.user.services.IUUserService;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * 用户登录相关，不需要做登录限制
 *
 */
@RestController
@Scope(value="prototype")
@RequestMapping("user")
public class UserLoginController {

	protected final static Logger logger = Logger.getLogger(UserLoginController.class);
	private Map<String,Object> resultMap = new LinkedHashMap<>();


	@Resource
	IUUserService iUUserService;

	/**
	 * 登录提交
	 * @param user		登录的UUser
	 * @param rememberMe	是否记住
	 * @param response		用来设置cookie。
	 * @return
	 */
	@RequiresGuest
	@RequestMapping(value="login",method=RequestMethod.POST)
	public Map<String,Object> submitLogin(UUser user, @RequestParam(defaultValue = "false") Boolean rememberMe,HttpServletRequest request, HttpServletResponse response){
        UsernamePasswordToken token = new UsernamePasswordToken(user.getEmail(), user.getPswd());

        token.setRememberMe(rememberMe);
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
			resultMap.put("status", 200);
			resultMap.put("message", "登录成功");

			//跳转地址
			resultMap.put("back_url", "./admin/admin.html");
            //更新登录时间 last login time
			user=iUUserService.findUserByEmail(user.getEmail());
            user.setLastLoginTime(new Date());
            iUUserService.updateByPrimaryKeySelective(user);
			Cookie cookie = new Cookie("current", URLEncoder.encode(user.toString(), "utf-8"));
			cookie.setPath(request.getContextPath());
			response.addCookie(cookie);
		/**
		 * 这里其实可以直接catch Exception，然后抛出 message即可，但是最好还是各种明细catch 好点。。
		 */
		} catch (AccountException e) {
			resultMap.put("status", 500);
			resultMap.put("message", e.getMessage());
		}catch (Exception e){
			resultMap.put("status", 500);
			resultMap.put("message", "帐号或密码不正确");
		}

		return resultMap;
	}

	/**
	 * 退出
	 * @return
	 * 本该返回Map的json对象，但为了方便，先做后台跳转
	 */

	@RequiresAuthentication
	@RequestMapping(value="logout",method =RequestMethod.GET)
	public ModelAndView logout(){
		try {
			SecurityUtils.getSubject().logout();
			resultMap.put("status", 200);
		} catch (Exception e) {
			resultMap.put("status", 500);
			logger.error("errorMessage:" + e.getMessage());
			LoggerUtils.fmtError(getClass(), e, "退出出现错误，%s。", e.getMessage());
		}
		return new ModelAndView("redirect:/static/login.html");
	}


	@RequiresGuest
	@RequestMapping(value = "register",method = RequestMethod.POST)
	public Map<String, Object> register(String vcode,UUser user,String pswd1){

		String svcode = (String)SecurityUtils.getSubject().getSession().getAttribute("vcode");
		System.out.println(svcode+"===>"+vcode);
		if(null==vcode||null==user.getPswd()||null==user.getEmail()||"".equals(user.getEmail())){
			resultMap.put("status",500);
			resultMap.put("message", "所有字段必填");
		}
		if(!vcode.equals(svcode)){
			resultMap.put("status",500);
			resultMap.put("message", "验证码不正确");
			return resultMap;
		}if (!user.getPswd().equals(pswd1)){
			resultMap.put("status",500);
			resultMap.put("message", "密码不一致");
		}
		try {
			user.setCreateTime(new Date());

			String md5 = MathUtil.getMD5(user.getPswd(), user.getEmail());
			user.setPswd(md5);
			resultMap.putAll(iUUserService.userRegister(user));
			resultMap.put("back_url", "./login.html");
		}catch (Exception e){
			LoggerUtils.fmtError(getClass(), "用户[%S]注册失败", user.getEmail());
		}

		return resultMap;
	}
}
