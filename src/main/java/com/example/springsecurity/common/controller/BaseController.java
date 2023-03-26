package com.example.springsecurity.common.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.common.utils.SecurityUtils;
import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.security.entity.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * web层通用数据处理
 */
public class BaseController  {

    /*每页数据量*/
    public static final String PAGE_SIZE = "pageSize";
    /*当前页码*/
    public static final String PAGE_NUM = "pageNum";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 返回成功
     */
    public Ret success(){
        return Ret.success();
    }

    /**
     * 返回失败消息
     */
    public Ret error(){
        return Ret.error();
    }

    /**
     * 返回成功消息
     */
    public Ret success(String message){
        return Ret.success(message);
    }
    
    /**
     * 返回成功消息
     */
    public Ret success(Object data){
        return Ret.success(data);
    }

    /**
     * 返回失败消息
     */
    public Ret error(String message){
        return Ret.error(message);
    }

    /**
     * 返回警告消息
     */
    public Ret warn(String message){
        return Ret.warn(message);
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
    public String getUserId(){
        return getLoginUser().getUserId();
    }

    /**
     * 获取登录用户名
     */
    public String getUsername()
    {
        return getLoginUser().getUsername();
    }

    /**
     * 返回分页校验错误
     */
    public Ret pageError(){
        return Ret.error(PAGE_NUM +"或"+PAGE_SIZE +"不能为空！");
    }

    /**
     * 获取前端传的页码
     */
    public Page getPageParam(Map<String,Object> param){
        //当前页
        String pageSize = String.valueOf(param.get(PAGE_SIZE));
        if(StringUtils.isEmpty(pageSize) || "null".equals(pageSize)){
            return null;
        }
        //每页的数据量
        String pageNum = String.valueOf(param.get(PAGE_NUM));
        if(StringUtils.isEmpty(pageNum) || "null".equals(pageNum)){
            return null;
        }
        Page page = new Page<>(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        return page;
    }


}
