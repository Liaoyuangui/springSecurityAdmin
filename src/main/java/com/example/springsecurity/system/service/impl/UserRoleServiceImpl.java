package com.example.springsecurity.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecurity.system.dao.UserRoleDao;
import com.example.springsecurity.system.entity.UserRole;
import com.example.springsecurity.system.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * (UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-03-23 12:30:19
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements UserRoleService {

}

