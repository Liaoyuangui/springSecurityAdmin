package com.example.springsecurity.security.filter;

import com.example.springsecurity.common.utils.Res.HttpStatus;
import com.example.springsecurity.common.utils.Res.ResponseUtil;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.security.entity.LoginUser;
import com.example.springsecurity.security.service.TokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器 验证token有效性
 */
@Component
@Log4j2
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException{
        String token = tokenService.getToken(request);
        String requestURI = request.getRequestURI();
        // token为null或空，则交给其他过滤器。此时只有开放接口可被访问。
        if (StringUtils.isEmpty(token)) {
            //判断是否是系统登录后的首页，如果是没有登录不能直接访问
            if("/system/index".equals(requestURI)){
                response.sendRedirect("/toLogin");
                return;
            }
            chain.doFilter(request, response);
            return ;
        }
        /*
         *若token不为空，则一定先判断token是否有效。若无效，则直接返回token无效，即使当前接口为开放接口。
         */
        // token不为空，则检查token是否有效。 loginUser 不为空则是有效的，否则无效
        LoginUser loginUser = tokenService.getLoginUser(request);
        //判断用户是否为空
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityContextHolder.getContext().getAuthentication())){
            //刷新登录过期时间
            tokenService.verifyToken(loginUser);
            /*设置Authentication */
            logger.debug("*****当前登录用户："+loginUser.getUsername() +"*****权限信息："+loginUser.getAuthorities());
            //System.out.println("*****当前登录用户："+loginUser.getUsername() +"；*****权限信息："+loginUser.getAuthorities());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        }else{
            // 判断是否是登录操作，是不管。 不是token无效，则告知前端。不再继续调用Filter链。
            if(requestURI.equals("/login")){
                chain.doFilter(request, response);
            }else{
                //判断是否是系统登录后的首页，如果是没有登录不能直接访问
                if("/system/index".equals(requestURI)){
                    response.sendRedirect("/toLogin");
                    return;
                }else{
                    //接口访问
                    //token无效，则告知前端。不再继续调用Filter链。
                    int code = HttpStatus.FORBIDDEN;
                    String msg = "请求访问认证失败，原因：可能登录已过期，请重新登录再试!";
                    ResponseUtil.out(request,response, Ret.error(code, msg));
                }
            }
        }

    }
}
