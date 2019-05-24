package org.joker.shirodemo.permission.services.impl;

import org.joker.shirodemo.common.dao.UPermissionMapper;
import org.joker.shirodemo.common.model.UPermission;
import org.joker.shirodemo.permission.services.IPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @Author joker
 * @Date 3/31/19 4:50 PM
 * @Description
 */
@Service
@Transactional
public class PermissionServiceImpl implements IPermissionService {

    @Resource
    UPermissionMapper uPermissionMapper;

    @Override
    public Set<String> findPermissionByUserId(Long userId) {

        return uPermissionMapper.findPermissionByUserId(userId);
    }

    @Override
    public List<UPermission> findAll() {
        return uPermissionMapper.findAll();
    }
}
