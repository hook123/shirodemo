package org.joker.shirodemo.user.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.joker.shirodemo.common.dao.UUserMapper;
import org.joker.shirodemo.common.model.UUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author joker
 * @Date 3/22/19 7:21 PM
 * @Description
 */
@RequiresPermissions("member")
@RestController
@RequestMapping("member")
public class MemberController {

    @Resource
    UUserMapper uUserMapper;


    /**
     * 用户列表管理
     * @param pn  页码
     * @param ps    每页大小
     * @return
     */
    @RequiresPermissions(value = "/member/list")
    @RequestMapping(value="list",method = RequestMethod.GET)
    public PageInfo<UUser> userList(@RequestParam(defaultValue = "1") Integer pn, @RequestParam(defaultValue = "10") Integer ps){
        PageHelper.startPage(pn,ps);
        List<UUser> all = uUserMapper.findAll();
        PageInfo<UUser> pageInfo = new PageInfo<UUser>(all);
        return pageInfo;
    }

    @RequiresPermissions(value = "getAllSession")
    @RequestMapping(value = "getAllSession",method = RequestMethod.GET)
    public Map getAllSession(){
        Map map =new HashMap();

        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager)SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
        SessionDAO sessionDAO = sessionManager.getSessionDAO();
        Collection<Session> activeSessions = sessionDAO.getActiveSessions();
        for (Session s: activeSessions) {
            map.put(s.getId(), s);
        }
        return map;
    }
}
