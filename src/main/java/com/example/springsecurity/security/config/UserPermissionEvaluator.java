package com.example.springsecurity.security.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springsecurity.security.service.SysPermissionService;
import com.example.springsecurity.system.dao.UserDao;
import com.example.springsecurity.system.entity.Role;
import com.example.springsecurity.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserPermissionEvaluator implements PermissionEvaluator {
    @Resource
    private SysPermissionService permissionService;

    @Resource
    private UserDao userMapper;

    /**
     * hasPermission鉴权方法，供前端使用,前端可以使用 sec:hasPermission('','')
     * @Param  permission 请求按钮的权限
     * @Return boolean 是否通过
     * https://blog.csdn.net/self_knowledge/article/details/119613813
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object permission) {
        Object principal = authentication.getPrincipal();
        boolean hasPermission = false;
        if(principal != null && principal instanceof UserDetails){
            String name = ((UserDetails) principal).getUsername();
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",name);
            List<User> users = userMapper.selectList(queryWrapper);
            User sysUser = users.get(0);
            /* Set<String> permissions = new HashSet<>();
            for(Role role : sysUser.getRoles()){
                permissions.add(role.getRoleKey());
            } */
            Set<String> permissions = permissionService.getMenuPermission(sysUser);
            //如果有这个权限返回true
            if (permissions.contains(permission.toString())){
                return true;
            }
        }
        return hasPermission;
    }
    
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
