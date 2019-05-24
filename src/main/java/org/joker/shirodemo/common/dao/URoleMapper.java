package org.joker.shirodemo.common.dao;

import org.joker.shirodemo.common.model.URole;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface URoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(URole record);

    int insertSelective(URole record);

    URole selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(URole record);

    int updateByPrimaryKey(URole record);

	Set<String> findRoleByUserId(Long id);

	List<URole> findNowAllPermission(Map<String, Object> map);
	
	void initData();

	List<URole> findAll();
}