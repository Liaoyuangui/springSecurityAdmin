package com.example.springsecurity.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.system.dao.RoleMenuDao;
import com.example.springsecurity.system.entity.RoleMenu;
import com.example.springsecurity.system.service.RoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-03-23 12:50:14
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuDao, RoleMenu> implements RoleMenuService {

    @Resource
    RoleMenuDao roleMenuDao;

    @Override
    public Ret queryListByUserId(String roleId) {
        List<Map<String,Object>> list = roleMenuDao.queryListByUserId(roleId);
        return Ret.success(buildMenuList(list));
    }

    @Override
    @Transactional
    public Ret saveRoleMenu(String roleId, List<String> menuList) {
        //先删除用户原来分配的数据
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        this.remove(queryWrapper);
        //保存
        List<RoleMenu> list = new ArrayList<>();
        for (int i = 0; i < menuList.size(); i++) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuList.get(i));
            list.add(roleMenu);
        }
        this.saveBatch(list,list.size());
        return Ret.success("保存成功！");
    }

    /***
     * 组装菜单结构
     * @param menuList 所有的菜单数据
     * @return
     */
    private List<Map<String,Object>> buildMenuList(List<Map<String,Object>> menuList){
        List<Map<String,Object>> finalNode = new ArrayList<>();
        //根目录
        List<Map<String,Object>> collect = menuList.stream().filter(a -> "M".equals(a.get("menu_type").toString())).collect(Collectors.toList());
        for(Map<String,Object> menu : collect){
            finalNode.add(selectChildren(menu,menuList));
        }
        return finalNode;
    }

    /***
     * 查找子节点
     * 一层里面有二层，二层里面有三层...
     * @return
     */
    private Map<String,Object> selectChildren(Map<String,Object> menu,List<Map<String,Object>> menuList){
        menu.put("children",new ArrayList<Map<String,Object>>());
        //根据id 和 parentId 获取到这个主菜单下的所有子菜单
        List<Map<String,Object>> collect = menuList.stream().filter(a -> menu.get("id").toString().equals(a.get("parent_id").toString())).collect(Collectors.toList());
        if(collect.size() > 0){
            for(Map<String,Object> m : collect){
                List<Map<String,Object>> children = (List<Map<String, Object>>) menu.get("children");
                children.add(selectChildren(m,menuList)); //这里使用递归算法实现
            }
        }
        return menu;
    }
}

