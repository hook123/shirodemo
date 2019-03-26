package org.joker.shirodemo.user.controller;


import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.joker.shirodemo.common.model.UUser;
import org.joker.shirodemo.common.utils.LoggerUtils;
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
@RequestMapping("/user")
public class UserLoginController {

	protected final static Logger logger = Logger.getLogger(UserLoginController.class);
	private Map<String,Object> resultMap = new LinkedHashMap<>();


	@Resource
	IUUserService iUUserService;

	/**
	 * 登录提交
	 * @param user		登录的UUser
	 * @param rememberMe	是否记住
	 * @param request		request，用来取登录之前Url地址，用来登录后跳转到没有登录之前的页面。
	 * @return
	 */
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	public Map<String,Object> submitLogin(UUser user,@RequestParam(defaultValue = "false") Boolean rememberMe,HttpServletRequest request){
        UsernamePasswordToken token = new UsernamePasswordToken(user.getEmail(), user.getPswd());
        token.setRememberMe(rememberMe);
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            user = (UUser) subject.getPrincipal();
			resultMap.put("status", 200);
			resultMap.put("message", "登录成功");
			
			/**
			 * shiro 获取登录之前的地址
			 * 之前0.1版本这个没判断空。
			 */

			SavedRequest savedRequest = WebUtils.getSavedRequest(request);
			String url = null ;
			if(null != savedRequest){
				url = savedRequest.getRequestUrl();
			}
			/**
			 * 我们平常用的获取上一个请求的方式，在Session不一致的情况下是获取不到的
			 * String url = (String) request.getAttribute(WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE);
			 */
			LoggerUtils.fmtDebug(getClass(), "获取登录之前的URL:[%s]",url);
			//如果登录之前没有地址，那么就跳转到首页。
			if(StringUtils.isBlank(url)){
				url = request.getContextPath() + "/admin/admin.html";
			}
			//跳转地址
			resultMap.put("back_url", url);

            //更新登录时间 last login time
            user.setLastLoginTime(new Date());
            iUUserService.updateByPrimaryKeySelective(user);
		/**
		 * 这里其实可以直接catch Exception，然后抛出 message即可，但是最好还是各种明细catch 好点。。
		 */
		} catch (DisabledAccountException e) {
			resultMap.put("status", 500);
			resultMap.put("message", "帐号已经禁用。");
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "帐号或密码错误");
		}

		return resultMap;
	}

	/**
	 * 退出
	 * @return
	 * 本该返回Map的json对象，但为了方便，先做后台跳转
	 */
	@RequestMapping(value="logout.do",method =RequestMethod.GET)
	public ModelAndView logout(){
		try {
			SecurityUtils.getSubject().logout();
			resultMap.put("status", 200);
		} catch (Exception e) {
			resultMap.put("status", 500);
			logger.error("errorMessage:" + e.getMessage());
			LoggerUtils.fmtError(getClass(), e, "退出出现错误，%s。", e.getMessage());
		}
		return new ModelAndView("redirect:/login.html");
	}

	@RequestMapping(value = "register.do",method = RequestMethod.POST)
	public Map<String, Object> register(String vcode,UUser user){


		return resultMap;
	}
}
