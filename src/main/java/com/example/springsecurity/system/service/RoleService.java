package com.example.springsecurity.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.system.entity.Role;

import java.util.Map;
import java.util.Set;

/**
 * (Role)表服务接口
 *
 * @author makejava
 * @since 2023-03-23 12:50:31
 */
public interface RoleService extends IService<Role> {

    /**
     * @Description 根据用户id查询角色权限标识
     * @Author liaoyuangui
     * @Date 2023/3/14 10:31
     * @param
     * @return java.util.Set<java.lang.String>
     **/
    Set<String> selectRolePermissionByUserId(String userId);

    /**
     * @Description  查询角色列表
     * @Author liaoyuangui
     * @param page
     * @param param
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    Ret getRoleList(Page page, Map<String, Object> param);

    /**
     * @Description 删除角色
     * @Author liaoyuangui
     * @param ids
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    Ret delete(String ids);

    /**
     * @Description 添加或编辑角色
     * @Author liaoyuangui
     * @param role
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    Ret addOrUpdateRole(Role role);
}

