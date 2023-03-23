package com.example.springsecurity.security.controller;

import com.example.springsecurity.common.constant.Constants;
import com.example.springsecurity.common.utils.Res.AjaxResult;
import com.example.springsecurity.security.service.SysLoginService;
import com.example.springsecurity.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: Liaoyuangui
 * @Date: 2023/3/8 15:30
 * @Description:
 */
@RestController
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;

    /**
     * 登录方法
     *
     * @param user 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody User user){
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(user.getUsername(), user.getPassword());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }
}
