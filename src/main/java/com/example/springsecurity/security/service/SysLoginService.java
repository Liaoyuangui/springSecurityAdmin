package com.example.springsecurity.security.service;

import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.security.entity.LoginUser;
import com.example.springsecurity.exception.ServiceException;
import com.example.springsecurity.security.entity.WxAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import sun.security.util.SecurityConstants;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @Auther: Liaoyuangui
 * @Date: 2023/3/8 15:34
 * @Description:
 */
@Service
public class SysLoginService {
    @Resource
    @Qualifier("sysAuthenticationManager")  //Qualifier 有多个bean时避免冲突，指定具体的bean
    AuthenticationManager authenticationManager;
    @Autowired
    TokenService tokenService;

    /***
     * 用户名密码登录
     * @param username
     * @param password
     * @return
     */
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

    /***
     * 微信登录
     * @param openId 微信的openId
     * @return
     */
    public String wxLogin(String openId){
        // 登录前置校验
        if(StringUtils.isEmpty(openId)){
            throw new ServiceException("openId不能为空");
        }
        // 用户验证
        Authentication authentication = null;
        try{
            WxAuthenticationToken wxAuthenticationToken = new WxAuthenticationToken(openId);
            // 会执行ProviderManager 然后添加我们定义的WxAuthenticationProvider，然后执行authenticate
            // authenticate中我们会去调用UserDetailsServiceImpl.loadUserByUsername 验证登录
            authentication = authenticationManager.authenticate(wxAuthenticationToken);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("微信授权登录失败！");
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }



}
