package com.example.springsecurity.security.controller;

import com.example.springsecurity.common.constant.Constants;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.security.service.SysLoginService;
import com.example.springsecurity.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


/**
 常用的注解：
 @PreAuthorize 进入方法前的权限验证，如@PreAuthorize("hasAuthority('manager')")
 @PostAuthorize 使用频率并不高，在方法执行之后再进行权限验证
 基于角色和权限进行访问控制
 Spring Security提供了四个方法用于角色和权限的访问控制。
 通过这些方法，对用户是否具有某个或某些权限，进行过滤访问。
 对用户是否具备某个或某些角色，进行过滤访问。
 1.hasAuthority方法
 判断当前主题是否有指定的权限，有返回true，否则返回false
 该方法适用于只拥有一个权限的用户。
 2.hasAnyAuthority方法
 适用于一个主体有多个权限的情况，多个权限用逗号隔开。
 3.hasRole方法
 如果用户具备给定角色就允许访问，否则报403错误。
 hasRole需要在设置时加上“ROLE_”前缀，因为通过源码hasRole方法给自定义的角色名前加上了“ROLE_”前缀
 4.hasAnyRole方法
 设置多个角色，多个角色之间使用逗号隔开，只要用户具有某一个角色，就能访问。
 **/



/**
 * @Auther: Liaoyuangui
 * @Date: 2023/3/8 15:30
 * @Description:
 */
@RestController
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;

    /***
     * 跳转登录页面
     */
    @RequestMapping(path = {"/toLogin","/"})
    public ModelAndView toLogin(){
        return new ModelAndView("login");
    }

    /**
     * 登录方法
     *
     * @param user 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public Ret login(@RequestBody User user){
        Ret ret = Ret.success();
        // 生成令牌
        String token = loginService.login(user.getUsername(), user.getPassword());
        ret.set(Constants.TOKEN, token);
        return ret;
    }
}
