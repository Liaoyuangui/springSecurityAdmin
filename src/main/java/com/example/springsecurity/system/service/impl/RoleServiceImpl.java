package com.example.springsecurity.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.system.dao.RoleDao;
import com.example.springsecurity.system.entity.Menu;
import com.example.springsecurity.system.entity.Role;
import com.example.springsecurity.system.entity.User;
import com.example.springsecurity.system.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Override
    public Ret getRoleList(Page page, Map<String, Object> param) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        Object roleName = param.get("roleName");
        if(ObjectUtil.isNotNull(roleName)){
            queryWrapper.like("role_name",roleName);
        }
        queryWrapper.orderByAsc("id");
        Page dataPage = roleDao.selectPage(page, queryWrapper);
        return Ret.getPageOkResult(dataPage);
    }

    @Override
    @Transactional
    public Ret delete(String ids) {
        if(StringUtils.isEmpty(ids)){
            return Ret.error("请选择数据进行删除！");
        }
        String[] split = ids.split(",");
        List<String> list = Arrays.asList(split);
        if(list.size() < 1){
            return Ret.error("请选择数据进行删除！");
        }
        int i = roleDao.deleteBatchIds(list);
        if(i > 0){
            return Ret.success("删除成功!");
        }
        return Ret.error("删除失败！");
    }

    @Override
    public Ret addOrUpdateRole(Role role) {
        if(StringUtils.isEmpty(role.getRoleName())){
            return Ret.error("角色名称不能为空！");
        }
        if(StringUtils.isEmpty(role.getId())){
            //判断角色名称是否存在
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_name",role.getRoleName());
            List<Role> roles = roleDao.selectList(queryWrapper);
            if(roles.size() > 0){
                return Ret.error("该角色名已存在，请重新输入！");
            }
            //新增，生成role_key
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmsssss");
            String format = sdf.format(new Date());
            role.setRoleKey("ROLE_"+format);
            roleDao.insert(role);
            return Ret.success("添加成功");
        }else{
            //修改
            //判断角色名称是否存在
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_name",role.getRoleName());
            queryWrapper.ne("id",role.getId()); //不是当前的这条
            List<Role> roles = roleDao.selectList(queryWrapper);
            if(roles.size() > 0){
                return Ret.error("该角色名已存在，请重新输入！");
            }
            roleDao.updateById(role);
            return Ret.success("修改成功！");
        }
    }


}

