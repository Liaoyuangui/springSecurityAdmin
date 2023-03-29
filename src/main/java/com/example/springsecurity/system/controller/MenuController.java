package com.example.springsecurity.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springsecurity.common.controller.BaseController;
import com.example.springsecurity.common.utils.JsonUtils;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.system.entity.Menu;
import com.example.springsecurity.system.entity.User;
import com.example.springsecurity.system.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * (Menu)表控制层
 *
 * @author makejava
 * @since 2023-03-23 13:09:15
 */
@RestController
@RequestMapping("system/menu")
public class MenuController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private MenuService menuService;

    /***
     * 视图
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/indexView")
    public ModelAndView indexView(){
        return new ModelAndView("pages/system/menu/menu_list");
    }

    /***
     * 查询菜单列表
     * @param param
     * @return
     */
    @PostMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    public Ret list(@RequestBody Map<String,Object> param){
        Page page = getPageParam(param);
        if(null == page){
            return pageError();
        }
        return menuService.getMenuList(page,param);
    }



    /**
     * 获取当前登录人的菜单列表
     */
    @GetMapping("/list")
    public Ret list(){
        //List<Map<String, Object>> menus = menuService.selectMenuList(getUserId());
        List<Menu> menus = menuService.selectMenuByUserId(getUserId());
        return success(menus);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public Ret selectOne(@PathVariable Serializable id) {
        return success(this.menuService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param menu 实体对象
     * @return 新增结果
     */
    @PostMapping("/add")
    public Ret insert(@RequestBody Menu menu) {
        if(menu.getMenuType().equals("M")){
            menu.setParentId("0");
        }else{
            String perms = menu.getPerms();
            String[] split = perms.split(":");
            if(split.length != 3){
                return error("权限标识格式有误，请输入xx:xx:xx格式！");
            }
        }
        menu.setCreateTime(new Date());
        return success(this.menuService.save(menu));
    }

    /**
     * 修改数据
     *
     * @param menu 实体对象
     * @return 修改结果
     */
    @PostMapping("/update")
    public Ret update(@RequestBody Menu menu) {
        if(menu.getMenuType().equals("M")){
            menu.setParentId("0");
        }else{
            String perms = menu.getPerms();
            String[] split = perms.split(":");
            if(split.length != 3){
                return error("权限标识格式有误，请输入xx:xx:xx格式！");
            }
        }
        menu.setUpdateTime(new Date());
        return success(this.menuService.updateById(menu));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @PostMapping("/delete")
    public Ret delete(@RequestBody String idList) {
        String ids = JsonUtils.getString(idList, "idList"); //1,2,3 这种格式
        if(StringUtils.isEmpty(ids)){
            return error("请选择删除的数据！");
        }
        return menuService.delete(ids);
    }

    /**
     * 查询所有的菜单不包含按钮
     * @return
     */
    @GetMapping("/queryAllMenuNotButton")
    public Ret queryAllMenuNotButton(){
        List<Menu> menus = menuService.queryAllMenuNotButton();
        return success(menus);
    }

}

