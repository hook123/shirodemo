package org.joker.shirodemo.core.shiro;


import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.joker.shirodemo.common.model.UUser;
import org.joker.shirodemo.common.utils.LoggerUtils;
import org.joker.shirodemo.core.shiro.cache.MySimpleByteSource;
import org.joker.shirodemo.permission.services.IPermissionService;
import org.joker.shirodemo.permission.services.IRoleService;
import org.joker.shirodemo.user.services.IUUserService;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;

/**
 * @Author joker
 * @Date 3/23/19 4:56 PM
 * @Description 授权认证
 */
public class MySampleRealm extends AuthorizingRealm {


    @Resource
    private IUUserService iUUserService;

    @Resource
    private IRoleService iRoleService;

    @Resource
    private IPermissionService iPermissionService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String primaryPrincipal = (String)principalCollection.getPrimaryPrincipal();
        UUser user = iUUserService.findUserByEmail(primaryPrincipal);

        SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();
        //根据用户ID查询角色（role），放入到Authorization里。
        Set<String> roles = iRoleService.findRoleByUserId(user.getId());
        info.setRoles(roles);
        //根据用户ID查询权限（permission），放入到Authorization里。
        Set<String> permissions = iPermissionService.findPermissionByUserId(user.getId());
        info.setStringPermissions(permissions);
        return info;
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
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getEmail(), user.getPswd(), new MySimpleByteSource(user.getEmail()), getName());
        return info;
    }
}
