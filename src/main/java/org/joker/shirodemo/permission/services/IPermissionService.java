package org.joker.shirodemo.permission.services;

import org.joker.shirodemo.common.model.UPermission;

import java.util.List;
import java.util.Set;

/**
 * @Author joker
 * @Date 3/31/19 4:18 PM
 * @Description
 */
public interface IPermissionService {

    Set<String> findPermissionByUserId(Long userId);

    List<UPermission> findAll();
}
