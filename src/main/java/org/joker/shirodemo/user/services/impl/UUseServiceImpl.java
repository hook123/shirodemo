package org.joker.shirodemo.user.services.impl;

import org.joker.shirodemo.common.dao.UUserMapper;
import org.joker.shirodemo.common.model.UUser;
import org.joker.shirodemo.user.services.IUUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author joker
 * @Date 3/22/19 7:27 PM
 * @Description
 */
@Service
@Transactional
public class UUseServiceImpl implements IUUserService {


    @Resource
    UUserMapper uUserMapper;

    @Override
    public UUser findUserByEmail(String email) {
        UUser user = uUserMapper.findUserByEmail(email);
        return user;
    }

    @Override
    public int updateByPrimaryKeySelective(UUser user) {
        return uUserMapper.updateByPrimaryKeySelective(user);
    }


}
