package com.example.springsecurity.common.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.springsecurity.common.utils.Res.AjaxResult;
import com.example.springsecurity.common.utils.SecurityUtils;
import com.example.springsecurity.security.entity.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * web层通用数据处理
 */
public class BaseController  {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 返回成功
     */
    public AjaxResult success(){
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(){
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message){
        return AjaxResult.success(message);
    }
    
    /**
     * 返回成功消息
     */
    public AjaxResult success(Object data){
        return AjaxResult.success(data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message){
        return AjaxResult.error(message);
    }

    /**
     * 返回警告消息
     */
    public AjaxResult warn(String message){
        return AjaxResult.warn(message);
    }

    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser(){
        return SecurityUtils.getLoginUser();
    }

    /**
     * 获取登录用户id
     */
    public Integer getUserId(){
        return getLoginUser().getUserId();
    }

    /**
     * 获取登录用户名
     */
    public String getUsername()
    {
        return getLoginUser().getUsername();
    }
}
