package com.example.springsecurity.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.system.entity.Menu;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * (Menu)表服务接口
 *
 * @author makejava
 * @since 2023-03-23 13:09:15
 */
public interface MenuService extends IService<Menu> {
    /**
     * @Description 根据用户查询菜单权限标识
     * @Author liaoyuangui
     * @Date 2023/3/14 15:51
     * @param userId
     * @return java.util.Set<java.lang.String>
     **/
    Set<String> selectMenuPermsByUserId(String userId);

    /**
     * @Description 根据用户权限查询菜单列表
     * @Author liaoyuangui
     * @Date 2023/3/15 9:56
     * @param menu
     * @param userId
     * @return java.util.List<com.example.springsecurity.system.entity.Menu>
     **/
    List<Menu> selectMenuList(Menu menu, String userId);

    /**
     * @Description 根据用户权限查询菜单列表
     * @Author liaoyuangui
     * @Date 2023/3/15 9:56
     * @param userId 用户id
     * @return java.util.List<com.example.springsecurity.system.entity.Menu>
     **/
    List<Map<String,Object>> selectMenuList(String userId);

    /***
     * 根据用户权限查询菜单列表
     * @param userId
     * @return
     */
    List<Menu> selectMenuByUserId(String userId);

    /***
     * 查询菜单列表
     * @param page 分页
     * @param param 参数
     * @return
     */
    Ret getMenuList(Page page, Map<String,Object> param);
    /**
     * 查询所有的菜单不包含按钮
     * @return
     */
    List<Menu> queryAllMenuNotButton();
}

