package com.example.springsecurity.security.handle;

import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.common.utils.Res.HttpStatus;
import com.example.springsecurity.common.utils.Res.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 * 
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable{
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        int code = HttpStatus.UNAUTHORIZED;
        String msg = "请求访问：{"+request.getRequestURI()+"}，认证失败，无法访问系统资源";
        //response.sendRedirect("/toLogin");
        ResponseUtil.out(request,response,Ret.error(code, msg));
    }
}
