package org.joker.shirodemo.user.services;

import org.joker.shirodemo.common.model.UUser;

/**
 * @Author joker
 * @Date 3/22/19 7:26 PM
 * @Description
 */
public interface IUUserService {

    UUser login(String email, String pswd);

    int updateByPrimaryKeySelective(UUser user);
}
