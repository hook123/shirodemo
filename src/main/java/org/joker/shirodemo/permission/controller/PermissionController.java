package org.joker.shirodemo.permission.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joker.shirodemo.common.model.UPermission;
import org.joker.shirodemo.permission.services.IPermissionService;
import org.joker.shirodemo.user.controller.UserLoginController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author joker
 * @Date 4/1/19 12:56 AM
 * @Description
 */
@RestController
@RequestMapping("permission")
public class PermissionController {
    protected final static Logger logger = Logger.getLogger(UserLoginController.class);
    private Map<String,Object> resultMap = new LinkedHashMap<>();

    @Resource
    IPermissionService iPermissionService;

    @RequiresPermissions("/permission/list")
    @RequestMapping(value = "list",method = RequestMethod.GET)
    public PageInfo<UPermission> list(@RequestParam(defaultValue = "1") Integer pn, @RequestParam(defaultValue = "10") Integer ps){
        PageHelper.startPage(pn, ps);
        List<UPermission> all = iPermissionService.findAll();
        PageInfo<UPermission> pageInfo = new PageInfo<>(all);
        return pageInfo;
    }

    @RequestMapping(value = "all",method = RequestMethod.GET)
    public List<UPermission> list(){
        return iPermissionService.findAll();
    }
}
