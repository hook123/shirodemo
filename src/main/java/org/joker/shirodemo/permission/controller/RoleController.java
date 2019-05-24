package org.joker.shirodemo.permission.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joker.shirodemo.common.model.UPermission;
import org.joker.shirodemo.common.model.URole;
import org.joker.shirodemo.common.model.URolePermission;
import org.joker.shirodemo.common.utils.LoggerUtils;
import org.joker.shirodemo.permission.services.IRoleService;
import org.joker.shirodemo.user.controller.UserLoginController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author joker
 * @Date 4/1/19 9:45 PM
 * @Description
 */

@RestController
@RequestMapping("role")
public class RoleController {

    protected final static Logger logger = Logger.getLogger(UserLoginController.class);
    private Map<String,Object> resultMap = new HashMap<>();

    @Resource
    IRoleService iRoleService;


    @RequiresPermissions("/role/list")
    @RequestMapping(value = "list",method = RequestMethod.GET)
    public PageInfo<URole> list(@RequestParam(defaultValue = "1") Integer pn, @RequestParam(defaultValue = "10") Integer ps){
        PageHelper.startPage(pn, ps);
        List<URole> all = iRoleService.findAll();
        PageInfo<URole> pageInfo = new PageInfo<>(all);
        return pageInfo;
    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Map<String,Object> add(URole role){

        if(null==role.getName()||role.getName().length()<4||null==role.getType()||role.getType().length()<6){
            resultMap.put("status",500);
            resultMap.put("message", "所有请按要求填写");
            return resultMap;
        }
        try {

            iRoleService.save(role);
            resultMap.put("status",200);
            resultMap.put("message", "添加成功");
        }catch (Exception e){
            LoggerUtils.fmtError(getClass(), "添加角色[%S]失败", role.getName());
        }
        return resultMap;
    }

    @RequestMapping(value = "getPermission",method = RequestMethod.GET)
    public Map<String,Object> getPermission(Long roleId){

        if(null==roleId){
            resultMap.put("status",500);
            resultMap.put("message", "参数有误");
            return resultMap;
        }
        try {

            URole permissionByRoleId = iRoleService.findPermissionByRoleId(roleId);
            resultMap.put("status",200);
            resultMap.put("message", "查询成功");
            resultMap.put("data",permissionByRoleId );
        }catch (Exception e){
            LoggerUtils.fmtError(getClass(), "获取角色[%S]的权限失败", roleId);
        }
        return resultMap;
    }
    public Map<String,Object> setPermission(Long roleId, @RequestParam(name = "checkPerm") Set<Long> permissions){

        if(null==roleId){
            resultMap.put("status",500);
            resultMap.put("message", "参数有误");
            return resultMap;
        }
        try {


            URole uRole = new URole();
            uRole.setId(roleId);
            List<UPermission> p = uRole.getPermissions();
            p.add

            URole permissionByRoleId = iRoleService.findPermissionByRoleId(roleId);
            resultMap.put("status",200);
            resultMap.put("message", "查询成功");
            resultMap.put("data",permissionByRoleId );
        }catch (Exception e){
            LoggerUtils.fmtError(getClass(), "获取角色[%S]的权限失败", roleId);
        }
        return resultMap;
    }
}
