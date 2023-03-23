package com.example.springsecurity.system.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * (Role)表实体类
 *
 * @author makejava
 * @since 2023-03-23 12:50:30
 */
@SuppressWarnings("serial")
public class Role extends Model<Role> {
    //主键
    private Integer id;
    //角色名称
    private String roleName;
    //权限标识
    private String roleKey;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
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

