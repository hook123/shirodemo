package org.joker.shirodemo.permission.services.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.joker.shirodemo.common.dao.URoleMapper;
import org.joker.shirodemo.common.dao.URolePermissionMapper;
import org.joker.shirodemo.common.dao.UUserRoleMapper;
import org.joker.shirodemo.common.model.UPermission;
import org.joker.shirodemo.common.model.URole;
import org.joker.shirodemo.common.model.URolePermission;
import org.joker.shirodemo.common.model.UUser;
import org.joker.shirodemo.permission.services.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author joker
 * @Date 3/31/19 4:42 PM
 * @Description
 */
@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

    @Resource
    URoleMapper uRoleMapper;
    @Resource
    URolePermissionMapper uRolePermissionMapper;
    @Resource
    UUserRoleMapper uUserRoleMapper;

    @Override
    public Set<String> findRoleByUserId(Long userId) {
        return uRoleMapper.findRoleByUserId(userId);
    }

    @Override
    public List<URole> findAll() {
        return uRoleMapper.findAll();
    }

    @Override
    public int save(URole record) {
        return uRoleMapper.insert(record);
    }

    @Override
    public URole findPermissionByRoleId(Long roleId) {
        return uRoleMapper.selectByPrimaryKey(roleId);
    }
    public Map<String,Object> addPermission2Role(Long roleId,Set<Long> pIds){
        clearPermission(roleId);

        Map<String,Object> resultMap = new HashMap<String, Object>();
        int count = 0;
        try {
            //如果ids,permission 的id 有值，那么就添加。没值象征着：把这个角色（roleId）所有权限取消。
            if(!pIds.isEmpty()){

                //添加新的。
                for (Long pid : pIds) {
                        URolePermission entity = new URolePermission(roleId,new Long(pid));
                        count += uRolePermissionMapper.insertSelective(entity);
                }
            }
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        } catch (Exception e) {
            resultMap.put("status", 200);
            resultMap.put("message", "操作失败，请重试！");
        }
        //清空拥有角色Id为：roleId 的用户权限已加载数据，让权限数据重新加载
        List<Long> userIds = uUserRoleMapper.findUserIdByRoleId(roleId);
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager)SecurityUtils.getSecurityManager();
        final CacheManager cacheManager = securityManager.getCacheManager();
        TokenManager.clearUserAuthByUserId(userIds);
        resultMap.put("count", count);
        return resultMap;



    }
    public int clearPermission(Long roleId){
        return uRolePermissionMapper.deleteByRid(roleId);
    }
}
