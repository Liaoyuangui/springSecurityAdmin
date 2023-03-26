package com.example.springsecurity.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.system.entity.User;

import java.util.Map;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2023-03-23 12:49:36
 */
public interface UserService extends IService<User> {

    Ret getList(Page page, Map<String, Object> param);


    /**
     * @Description  删除
     * @Author liaoyuangui
     * @Date 2023/3/17 16:08
     * @param ids
     * @return boolean
     **/
    boolean delete(String ids);

    Ret addUser(User user);

    /**
     * @Description  新增或修改用户
     * @Author liaoyuangui
     * @Date 2023/3/20 16:11
     * @param user
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    Ret addOrUpdateUser(User user);
}

