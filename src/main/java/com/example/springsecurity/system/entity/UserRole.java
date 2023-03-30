package com.example.springsecurity.system.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * (UserRole)表实体类
 *
 * @author makejava
 * @since 2023-03-23 12:30:19
 */
@SuppressWarnings("serial")
public class UserRole extends Model<UserRole> {
    //id
    //指定主键生成策略使用雪花算法（默认策略）
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    //用户id
    private String userId;
    //角色id
    private String roleId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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

