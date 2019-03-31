package org.joker.shirodemo.core.shiro;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.joker.shirodemo.common.model.UUser;
import org.joker.shirodemo.common.utils.LoggerUtils;
import org.joker.shirodemo.common.utils.MathUtil;
import org.joker.shirodemo.core.shiro.cache.MySimpleByteSource;
import org.joker.shirodemo.user.services.IUUserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author joker
 * @Date 3/23/19 4:56 PM
 * @Description 授权认证
 */
public class MySampleRealm extends AuthorizingRealm {


    @Resource
    private IUUserService iUUserService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }


    /**
     * 认证信息，主要针对用户登录，
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();

        if (null == username || "".equals(username) || null == token.getPassword() || "".equals(token.getPassword())) {
            throw new UnknownAccountException("用户名或密码不能为空");
        }

        UUser user = iUUserService.findUserByEmail(username);

        if (null == user) {
            throw new AccountException("帐号或密码不正确！");
            /**
             * 如果用户的status为禁用。那么就抛出<code>DisabledAccountException</code>
             */
        } else if (UUser._0.equals(user.getStatus())) {
            throw new DisabledAccountException("帐号已经禁止登录！");
        } else {

        }

        SimpleAuthenticationInfo info = null;
        try {
            info = new SimpleAuthenticationInfo(user, user.getPswd(), new MySimpleByteSource(user.getEmail()), getName());
        } catch (Exception e) {
            LoggerUtils.fmtDebug(this.getClass(), "用户：%s认证失败", user.getEmail());
        }
        return info;
    }
}
