package com.example.springsecurity.security.handle;

import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.common.utils.Res.HttpStatus;
import com.example.springsecurity.common.utils.Res.ResponseUtil;
import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.security.entity.LoginUser;
import com.example.springsecurity.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler{

    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     * 
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException{
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)){
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getUserId().toString());
        }
        int code = HttpStatus.SUCCESS;
        ResponseUtil.out(request,response, Ret.error(code, "退出成功") );
    }
}
