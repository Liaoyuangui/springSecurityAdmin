package com.example.springsecurity.common.utils.Res;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于处理响应（HttpServletResponse）的工具类
 */
public class ResponseUtil {

   public static void out(HttpServletRequest request, HttpServletResponse response, Object msg) {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.SUCCESS);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        /**
         * 响应跨域配置
         */
        // 响应标头指定 指定可以访问资源的URI路径
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        //响应标头指定响应访问所述资源到时允许的一种或多种方法
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        //设置 缓存可以生存的最大秒数
        response.setHeader("Access-Control-Max-Age", "3600");
        //设置  受支持请求标头
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        // 指示的请求的响应是否可以暴露于该页面。当true值返回时它可以被暴露
        response.setHeader("Access-Control-Allow-Credentials", "true");
        try {
            mapper.writeValue(response.getWriter(), msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
