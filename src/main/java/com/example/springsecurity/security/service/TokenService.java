package com.example.springsecurity.security.service;

import com.example.springsecurity.common.constant.Constants;
import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.common.utils.uuid.IdUtils;
import com.example.springsecurity.security.entity.LoginUser;
import com.example.springsecurity.redis.RedisCache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author ruoyi
 */
@Component
public class TokenService
{
    // 令牌自定义标识,请求头或参数传递的参数名
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request){
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)){
            try{
                //解析token
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                LoginUser user = redisCache.getCacheObject(userKey);
                return user;
            }
            catch (Exception e){
                //e.printStackTrace();
                System.out.println(">>>>>>>>解析token异常<<<<<<<<<<<");
                return null;
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser){
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())){
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String userId){
        if (StringUtils.isNotEmpty(userId)){
            String userKey = getTokenKey(userId);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser){
        String token = IdUtils.fastUUID();
        loginUser.setToken(token); //设置token
        //将用户信息存入redis
        refreshToken(loginUser);
        Map<String, Object> claims = new HashMap<>();
        //把用户id存进去
        claims.put(Constants.LOGIN_USER_KEY, loginUser.getUserId().toString());
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser){
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN){
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser){
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据用户id将loginUser缓存
        String userKey = getTokenKey(loginUser.getUserId().toString());
        //将用户信息缓存到redis中
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims){
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token){
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    public String getToken(HttpServletRequest request){
        //1. 从请求头中获取token
        String token = request.getHeader(header);
        if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
            // 2.从请求头中未获取到，再从请求参数获取
            token = request.getParameter(header);
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
            // 3.从cookie 中获取
            token = getTokenFromCookie(request,header);
        }
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)){
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * @Description  redis中存放token的key
     * @Author liaoyuangui
     * @Date 2023/3/9 14:26
     * @param userId  用户id作为key
     * @return java.lang.String
     **/
    private String getTokenKey(String userId){
        return Constants.LOGIN_TOKEN_KEY + userId;
    }

    /**
     * @Description  从Cookie中获取token
     * @Author liaoyuangui
     * @Date 2023/3/9 17:20
     * @param request
     * @return java.lang.String
     **/
    private String getTokenFromCookie(HttpServletRequest request,String tokenKey) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        int len = null == cookies ? 0 : cookies.length;
        if (len > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenKey)) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }
}
