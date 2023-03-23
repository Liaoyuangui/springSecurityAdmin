package com.example.springsecurity.security.service;

import com.example.springsecurity.common.utils.SecurityUtils;
import com.example.springsecurity.system.entity.User;
import com.example.springsecurity.system.service.MenuService;
import com.example.springsecurity.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户权限处理
 */
@Component
public class SysPermissionService{

    @Autowired
    private RoleService roleService;


    @Autowired
    private MenuService menuService;

    /**
     * 获取角色数据权限
     * 
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(User user){
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (SecurityUtils.isAdmin(user.getId())){
            roles.add("admin");
        }else{
            Set<String> set = roleService.selectRolePermissionByUserId(user.getId());
            roles.addAll(set);
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     * 
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(User user){
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (SecurityUtils.isAdmin(user.getId())){
            perms.add("*:*:*");
        }else{
            perms.addAll(menuService.selectMenuPermsByUserId(user.getId()));
        }
        return perms;
    }
}
