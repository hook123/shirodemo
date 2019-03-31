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

    @Override
    public Map<String, Object> userRegister(UUser record) {
        Map map = new HashMap();
        UUser user = uUserMapper.findUserByEmail(record.getEmail());

        if (null != user) {
            map.put("status", 500);
            map.put("message", user.getEmail() + "已注册");
            return map;
        }

        uUserMapper.insertSelective(record);
        map.put("status", 200);
        map.put("message", "注册成功");

        return map;
    }


}
