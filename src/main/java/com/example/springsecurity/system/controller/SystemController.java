package com.example.springsecurity.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Auther: Liaoyuangui
 * @Date: 2023/3/10 11:26
 * @Description:
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    /**
     * @Description  系统首页,只需要登录，不需要数据权限
     * @Author liaoyuangui
     * @Date 2023/3/10 18:42
     * @param
     * @return org.springframework.web.servlet.ModelAndView
     **/
    @GetMapping({"/index"})
    public ModelAndView index(){
        return new ModelAndView("index");
    }

    /**
     * @Description  欢迎页
     * @Author liaoyuangui
     * @Date 2023/3/10 18:41
     * @param
     * @return org.springframework.web.servlet.ModelAndView
     **/
    @GetMapping({"/welcome"})
    public ModelAndView mainView() {
        return new ModelAndView("pages/welcome");
    }


}
