package com.example.springsecurity.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecurity.system.dao.RoleDao;
import com.example.springsecurity.system.entity.Role;
import com.example.springsecurity.system.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * (Role)表服务实现类
 *
 * @author makejava
 * @since 2023-03-23 12:50:31
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    @Resource
    RoleDao roleDao;

    @Override
    public Set<String> selectRolePermissionByUserId(String userId) {
        return roleDao.selectRolePermissionByUserId(userId);
    }
}

