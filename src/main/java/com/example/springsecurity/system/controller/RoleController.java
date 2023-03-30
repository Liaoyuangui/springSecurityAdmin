package com.example.springsecurity.system.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springsecurity.common.controller.BaseController;
import com.example.springsecurity.common.utils.JsonUtils;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.system.entity.Role;
import com.example.springsecurity.system.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

/**
 * (Role)表控制层
 *
 * @author makejava
 * @since 2023-03-23 12:50:30
 */
@RestController
@RequestMapping("system/role")
public class RoleController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private RoleService roleService;

    /***
     * 视图
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/indexView")
    public ModelAndView indexView(){
        return new ModelAndView("pages/system/role/role_list");
    }

    /***
     * 分页查询角色列表
     * @param param
     * @return
     */
    @PostMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    public Ret list(@RequestBody Map<String,Object> param){
        Page page = getPageParam(param);
        if(null == page){
            return pageError();
        }
        return roleService.getRoleList(page,param);
    }



    /**
     * @Description 新增
     * @Author liaoyuangui
     * @Date 2023/3/29 16:13
     * @param role
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    @PostMapping("/add")
    //@PreAuthorize("@ss.hasPermi('system:role:add')")
    public Ret add(@RequestBody Role role){
        return roleService.addOrUpdateRole(role);
    }

    /**
     * @Description 修改
     * @Author liaoyuangui
     * @Date 2023/3/29 16:13
     * @param role
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    @PostMapping("/update")
    //@PreAuthorize("@ss.hasPermi('system:role:add')")
    public Ret update(@RequestBody Role role){
        return roleService.addOrUpdateRole(role);
    }

    /**
     * @Description 删除
     * @Author liaoyuangui
     * @Date 2023/3/29 16:13
     * @param idList
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    @PostMapping("/delete")
    //@PreAuthorize("@ss.hasPermi('system:role:delete')")
    public Ret delete(@RequestBody String idList){
        String ids = JsonUtils.getString(idList, "idList"); //1,2,3 这种格式
        if(StringUtils.isEmpty(ids)){
            return error("请选择删除的数据！");
        }
        return roleService.delete(ids);
    }



}

