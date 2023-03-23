package com.example.springsecurity.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecurity.system.dao.RoleMenuDao;
import com.example.springsecurity.system.entity.RoleMenu;
import com.example.springsecurity.system.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * (RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-03-23 12:50:14
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuDao, RoleMenu> implements RoleMenuService {

}

