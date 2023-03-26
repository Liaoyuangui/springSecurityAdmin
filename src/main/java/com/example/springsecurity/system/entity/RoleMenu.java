package com.example.springsecurity.system.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * (RoleMenu)表实体类
 *
 * @author makejava
 * @since 2023-03-23 12:50:14
 */
@SuppressWarnings("serial")
public class RoleMenu extends Model<RoleMenu> {
    //主键
    private String id;
    //权限id
    private String roleId;
    //菜单id
    private String menuId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

