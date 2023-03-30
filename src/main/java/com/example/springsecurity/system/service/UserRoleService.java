package com.example.springsecurity.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.system.entity.UserRole;

import java.util.List;

/**
 * (UserRole)表服务接口
 *
 * @author makejava
 * @since 2023-03-23 12:30:19
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * @Description 根据用户查询角色
     * @Author liaoyuangui
     * @param userId
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    Ret queryListByUserId(String userId);

    /**
     * @Description 保存用户角色
     * @Author liaoyuangui
     * @param userId
     * @param roleList
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    Ret saveUserRole(String userId, List<String> roleList);
}

