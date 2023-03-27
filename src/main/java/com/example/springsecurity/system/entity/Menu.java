package com.example.springsecurity.system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (Menu)表实体类
 *
 * @author makejava
 * @since 2023-03-23 13:09:15
 */
@SuppressWarnings("serial")
public class Menu extends Model<Menu> {
    //主键
    private String id;
    //菜单名称
    private String menuName;
    //父id
    private String parentId;
    //排序
    private Integer orderNum;
    //路径
    private String path;
    //权限标识
    private String perms;
    //类型，M目录 C菜单 F按钮
    private String menuType;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //备注
    private String remark;
    //图标
    private String icon;

    //父级菜单名称
    @TableField(exist = false)
    private String parentName;

    //子菜单
    @TableField(exist = false)
    private List<Menu> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getParentName() {
        return parentName;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
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

