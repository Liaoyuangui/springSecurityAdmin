package com.example.springsecurity.security.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springsecurity.security.entity.LoginUser;
import com.example.springsecurity.security.entity.WxAuthenticationToken;
import com.example.springsecurity.security.service.UserDetailsServiceImpl;
import com.example.springsecurity.system.dao.UserDao;
import com.example.springsecurity.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.annotation.Resource;
import java.util.List;

/***
 * 微信登录的Provider ， 用户名密码有一个框架自己实现的所以可以不用
 */
public class WxAuthenticationProvider implements AuthenticationProvider {

    // security框架中实现了UserDetailsService 的service
    //@Autowired
    private UserDetailsServiceImpl userDetailsService;

    //系统的userDao, 实现用户的增删改查
    @Resource
    private UserDao userDao;


    /***
     * 在SecurityConfig 中配置provider时，注入进来
     * @param userDetailsService
     */
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 取到authentication中的openId，根据openId查询信息，能查到信息表示登陆成功
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WxAuthenticationToken wxAuthenticationToken = (WxAuthenticationToken) authentication;
        String openId = wxAuthenticationToken.getPrincipal().toString(); //微信的openId
        //根据openId 去数据库中查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //todo 这里需要用openId去获取用户信息
        //模拟一个微信用户
        queryWrapper.eq("username","root");
        List<User> users = userDao.selectList(queryWrapper);
        if(users.size() < 1 || users.get(0) == null){
            throw new BadCredentialsException("登录失败，微信账号未绑定！");
        }
        //获取授权信息， 这里直接调用之前用户密码登录获取权限的那一套， 也可以自己定义service写自己的方法
        LoginUser loginUser = (LoginUser) userDetailsService.loadUserByUsername(users.get(0).getUsername());
        if(loginUser == null){
            throw new RuntimeException("登陆失败");
        }
        WxAuthenticationToken authenticationToken = new WxAuthenticationToken(loginUser);
        return authenticationToken;
    }

    /**
     * 配置当前Provider对应的wxAuthenticationToken
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (WxAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
