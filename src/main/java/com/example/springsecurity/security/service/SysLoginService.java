package com.example.springsecurity.security.service;

import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.security.entity.LoginUser;
import com.example.springsecurity.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auther: Liaoyuangui
 * @Date: 2023/3/8 15:34
 * @Description:
 */
@Service
public class SysLoginService {
    @Resource
    AuthenticationManager authenticationManager;
    @Autowired
    TokenService tokenService;

    public String login(String username, String password) {
        // 登录前置校验
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
              throw new ServiceException("用户名或密码为空");
        }
        // 用户验证
        Authentication authentication = null;
        try{
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername 验证登录
            authentication = authenticationManager.authenticate(authenticationToken);
        }catch (Exception e){
            e.printStackTrace();
            String msg = "登录失败!";
            if (e instanceof LockedException) {
                msg = "账户被锁定，登录失败!";
            }else if(e instanceof BadCredentialsException){
                msg = "账户名或密码输入错误，登录失败!";
            }else if(e instanceof DisabledException){
                msg = "账户被禁用，登录失败!";
            }else if(e instanceof AccountExpiredException){
                msg = "账户已过期，登录失败!";
            }else if(e instanceof CredentialsExpiredException){
                msg = "密码已过期，登录失败!";
            }else{
                msg = "登录失败!";
            }
            throw new ServiceException(msg);
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }
}
