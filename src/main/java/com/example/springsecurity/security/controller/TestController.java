package com.example.springsecurity.security.controller;

import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.security.entity.LoginUser;
import com.example.springsecurity.system.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
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
 * @Date: 2023/2/10 15:18
 * @Description:
 */
@Controller
public class TestController {

    //首页，不需要权限
    @GetMapping({"/index"})
    public String index() {
        return "index";
    }

    //首页，不需要权限
    @GetMapping({"/main/index"})
    public String adminIndex() {
        return "admin/index";
    }

    //自定义登录页面
    @GetMapping({"/","/toLogin"})
    public String toLogin() {
        return "login";
    }

    //vip1
    @GetMapping("vip/vip1")
    //@PreAuthorize("hasAuthority('system:vip:vip1')")
    @PreAuthorize("hasAnyRole('vip1')")
    public String vip1() {
        return "vip/vip1/index";
    }

    //vip2
    @GetMapping("vip/vip2")
    @PreAuthorize("hasAnyRole('vip2')")
    public String vip2() {
        return "vip/vip2/index";
    }

    //vip3
    @GetMapping("vip/vip3")
    @PreAuthorize("hasAnyRole('vip3')")
    public String vip3() {
        return "vip/vip3/index";
    }


    /**
     * @Description  获取登录人信息
     * @Author liaoyuangui
     * @Date 2023/2/14 9:41
     * @param
     * @return java.lang.Object
     **/
    @GetMapping("/getUserInfo")
    @ResponseBody
    public Object getUserInfo() {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            //已登录
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//获取用户信息

            //获取登录的用户名
            String username = authentication.getName();
            System.out.println("username : " + username);

            //用户的所有权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            System.out.println("authorities : " + authorities);


            /**
             * 如果要获取更详细的用户信息可以采用下面这种方法
             */
            //用户的基本信息
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = loginUser.getUser();
            System.out.println("user : " + user);

            //用户的id
            String userId = user.getId();
            System.out.println("userId: " + userId);

            //User其余信息可以用这种方式获取
            //List<Role> roles = user.getRoles();
            //String password = user.getPassword();
            //String username1 = user.getUsername();

            System.out.println("已登录账号：" + username);

            return Ret.success("已登录",user);
        } else {
            //未登录
            System.out.println("未登录...." );
            return Ret.success("未成功","");
        }


    }
}