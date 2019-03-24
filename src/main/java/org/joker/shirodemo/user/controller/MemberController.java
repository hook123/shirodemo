package org.joker.shirodemo.user.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.joker.shirodemo.common.dao.UUserMapper;
import org.joker.shirodemo.common.model.UUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author joker
 * @Date 3/22/19 7:21 PM
 * @Description
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    UUserMapper uUserMapper;


    /**
     * 用户列表管理
     * @param pn  页码
     * @param ps    每页大小
     * @return
     */
    @RequestMapping(value="/userlist.do",method = RequestMethod.GET)
    public PageInfo<UUser> userList(@RequestParam(defaultValue = "1") Integer pn, @RequestParam(defaultValue = "10") Integer ps){
        PageHelper.startPage(pn,ps);
        List<UUser> all = uUserMapper.findAll();
        PageInfo<UUser> pageInfo = new PageInfo<UUser>(all);
        return pageInfo;
    }
}
