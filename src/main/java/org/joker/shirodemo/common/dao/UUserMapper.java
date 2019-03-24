package org.joker.shirodemo.common.dao;

import org.joker.shirodemo.common.model.UUser;

import java.util.List;
import java.util.Map;


public interface UUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UUser record);

    int insertSelective(UUser record);

    UUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UUser record);

    int updateByPrimaryKey(UUser record);

	UUser findUserByEmail(String email);

	List <UUser> findAll();

//	List<URoleBo> selectRoleByUserId(Long id);

}