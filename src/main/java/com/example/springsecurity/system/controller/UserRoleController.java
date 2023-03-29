package com.example.springsecurity.system.controller;


import com.example.springsecurity.common.controller.BaseController;
import com.example.springsecurity.system.service.UserRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * (UserRole)表控制层
 *
 * @author makejava
 * @since 2023-03-23 12:30:19
 */
@RestController
@RequestMapping("system/userRole")
public class UserRoleController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private UserRoleService userRoleService;

    /***
     * 视图
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:userRole:list')")
    @GetMapping("/indexView")
    public ModelAndView indexView(){
        return new ModelAndView("pages/system/userRole/user_role_list");
    }



}

