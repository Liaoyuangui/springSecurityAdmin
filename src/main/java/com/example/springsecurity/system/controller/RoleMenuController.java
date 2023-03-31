package com.example.springsecurity.system.controller;


import com.example.springsecurity.common.controller.BaseController;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.system.service.RoleMenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * (RoleMenu)表控制层
 *
 * @author makejava
 * @since 2023-03-23 12:50:14
 */
@RestController
@RequestMapping("system/roleMenu")
public class RoleMenuController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private RoleMenuService roleMenuService;

    /**
     * @Description 视图
     * @Author liaoyuangui
     * @Date 2023/3/30 15:49
     * @param
     * @return org.springframework.web.servlet.ModelAndView
     **/
    @PreAuthorize("@ss.hasPermi('system:roleMenu:view')")
    @RequestMapping("/indexView")
    public ModelAndView view(){
        return new ModelAndView("pages/system/roleMenu/role_menu_list");
    }

    /**
     * @Description 根据角色查询菜单列表
     * @Author liaoyuangui
     * @param roleId
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    @GetMapping("/queryListByRoleId")
    public Ret queryListByRoleId(@RequestParam("roleId") String roleId){
        if(StringUtils.isEmpty(roleId)){
            return Ret.error("角色编号不能为空！");
        }
        return roleMenuService.queryListByUserId(roleId);
    }

    /**
     * @Description 保存
     * @Author liaoyuangui
     * @param roleMenu
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    @PostMapping("/saveRoleMenu")
    @PreAuthorize("@ss.hasPermi('system:roleMenu:save')")
    public Ret saveRoleMenu(@RequestBody Map<String,Object> roleMenu){
        Object roleId = roleMenu.get("roleId");
        if(null == roleId){
            return Ret.error("授权失败，未获取到角色信息");
        }
        List<String> menuList = (List<String>) roleMenu.get("menu");
        return  roleMenuService.saveRoleMenu(roleId.toString(),menuList);
    }

}

