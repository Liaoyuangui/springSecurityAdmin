package com.example.springsecurity.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springsecurity.common.utils.Res.AjaxResult;
import com.example.springsecurity.system.entity.User;

import java.util.List;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2023-03-23 12:49:36
 */
public interface UserService extends IService<User> {

    List<User> getList(User user);


    /**
     * @Description  删除
     * @Author liaoyuangui
     * @Date 2023/3/17 16:08
     * @param ids
     * @return boolean
     **/
    boolean delete(String ids);

    AjaxResult addUser(User user);

    /**
     * @Description  新增或修改用户
     * @Author liaoyuangui
     * @Date 2023/3/20 16:11
     * @param user
     * @return com.example.springsecurity.common.utils.Res.AjaxResult
     **/
    AjaxResult addOrUpdateUser(User user);
}

