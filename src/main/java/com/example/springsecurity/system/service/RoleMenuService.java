package com.example.springsecurity.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.system.entity.RoleMenu;

import java.util.List;

/**
 * (RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2023-03-23 12:50:14
 */
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * @Description 查询菜单列表
     * @Author liaoyuangui
     * @param roleId
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    Ret queryListByUserId(String roleId);

    /**
     * @Description 保存角色菜单信息
     * @Author liaoyuangui
     * @param roleId
     * @param menuList
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    Ret saveRoleMenu(String roleId, List<String> menuList);
}

