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
    private Integer id;
    //权限id
    private Integer roleId;
    //菜单id
    private Integer menuId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
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

