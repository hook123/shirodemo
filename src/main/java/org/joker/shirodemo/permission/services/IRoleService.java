package org.joker.shirodemo.permission.services;
import org.joker.shirodemo.common.model.URole;
import java.util.List;
import java.util.Set;

/**
 * @Author joker
 * @Date 3/31/19 4:15 PM
 * @Description
 */
public interface IRoleService {

    Set<String> findRoleByUserId(Long userId);
    List<URole> findAll();
    int save(URole record);
    URole findPermissionByRoleId(Long roleId);
}
