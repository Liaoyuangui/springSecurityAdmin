package com.example.springsecurity.common.utils.Res;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.springsecurity.common.utils.StringUtils;

import java.util.HashMap;

/**
 * 操作消息提醒
 */
public class Ret extends HashMap<String, Object>{
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    /*分页总数据量*/
    public static final String PAGE_TOTAL = "total";

    /**
     * 初始化一个新创建的 Ret 对象，使其表示一个空消息。
     */
    public Ret(){
    }

    /**
     * 初始化一个新创建的 Ret 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     */
    public Ret(int code, String msg){
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 Ret 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public Ret(int code, String msg, Object data){
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     * 
     * @return 成功消息
     */
    public static Ret success(){
        return Ret.success("操作成功");
    }

    /**
     * 返回成功数据
     * 
     * @return 成功消息
     */
    public static Ret success(Object data){
        return Ret.success("操作成功", data);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @return 成功消息
     */
    public static Ret success(String msg){
        return Ret.success(msg, null);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static Ret success(String msg, Object data){
        return new Ret(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static Ret warn(String msg){
        return Ret.warn(msg, null);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static Ret warn(String msg, Object data){
        return new Ret(HttpStatus.WARN, msg, data);
    }

    /**
     * 返回错误消息
     * 
     * @return 错误消息
     */
    public static Ret error(){
        return Ret.error("操作失败");
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @return 错误消息
     */
    public static Ret error(String msg){
        return Ret.error(msg, null);
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 错误消息
     */
    public static Ret error(String msg, Object data){
        return new Ret(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @return 错误消息
     */
    public static Ret error(int code, String msg){
        return new Ret(code, msg, null);
    }

    /**
     * 方便链式调用
     *
     * @param key 键
     * @param value 值
     * @return 数据对象
     */
    public Ret set(String key, Object value){
        super.put(key, value);
        return this;
    }

    /**
     * @Description 分页返回
     * @param page
     * @return com.gzzy.salesecond.common.Response.Ret
     **/
    public static Ret getPageOkResult(IPage page){
        return Ret.success().set(PAGE_TOTAL,page.getTotal()).set(DATA_TAG,page.getRecords());
    }
}
