package com.example.springsecurity.security.config;

import com.example.springsecurity.security.service.UserDetailsServiceImpl;
import com.example.springsecurity.security.filter.JwtAuthenticationTokenFilter;
import com.example.springsecurity.security.handle.AuthenticationEntryPointImpl;
import com.example.springsecurity.security.handle.LogoutSuccessHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Auther: Liaoyuangui
 * @Date: 2023/2/10 15:25
 * @Description:
 */
/**
 * 没有添加改配置，页面会强制跳转到springsecurity自己的登录页面
 * 参考链接：https://www.cnblogs.com/dw3306/p/12751373.html
 */
@Configuration
@EnableWebSecurity //启用Spring Security
@EnableGlobalMethodSecurity(prePostEnabled=true) //会拦截注解了@PreAuthrize注解的配置.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userService;

    /**
     *未授权处理
     **/
    @Autowired
    AuthenticationEntryPointImpl authenticationEntryPoint;
    /**
     *退出处理
     **/
    @Autowired
    LogoutSuccessHandlerImpl logoutSuccessHandler;
    /**
     * token认证过滤器
     */
    @Autowired
    JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * 跨域过滤器
     */
    @Resource
    private CorsFilter corsFilter;

    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    // 指定密码的加密方式，不然定义认证规则那里会报错
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return Objects.equals(charSequence.toString(), s);
            }
        };
    }

    //配置忽略掉的 URL 地址,一般用于js,css,图片等静态资源
    @Override
    public void configure(WebSecurity web) throws Exception {
        //web.ignoring() 用来配置忽略掉的 URL 地址，一般用于静态文件
        web.ignoring().antMatchers("/static/js/**", "/static/css/**","/static/fonts/**","/static/images/**","/static/lib/**","/static/img/**");
    }

    // （认证）配置用户及其对应的角色
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //数据在内存中定义，一般要去数据库取，jdbc中去拿，
        /**
         * 懒羊羊,灰太狼,喜羊羊,小灰灰分别具有vip0,vip1,vip2,vip3的权限
         * root则同时又vip0到vip3的所有权限
         */
        //Spring security 5.0中新增了多种加密方式，也改变了密码的格式。
        //要想我们的项目还能够正常登陆，需要修改一下configure中的代码。我们要将前端传过来的密码进行某种方式加密
        //spring security 官方推荐的是使用bcrypt加密方式。
       /* auth.inMemoryAuthentication()
                .withUser("懒羊羊").password("123").roles("vip0")
                .and()
                .withUser("灰太狼").password("123").roles("vip1")
                .and()
                .withUser("喜羊羊").password("123").roles("vip2")
                .and()
                .withUser("小灰灰").password("123").roles("vip3")
                .and()
                .withUser("root").password("123").roles("vip1","vip2","vip3")
                .and()
                .withUser("admin").password("123456").roles("index","vip1","vip2","vip3");*/

       //从数据库获取，对应的角色参考数据库中的user_role 表
        //配置service，该service需要实现UserDetailsService接口, 然后登录的时候会自动去查询该接口中的方法
        //数据库中的权限数据需以ROLE_ 开头， 如vip1 -> ROLE_vip1
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());

    }



    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 禁用HTTP响应标头
                .headers().cacheControl().disable().and()
                // 权限认证失败处理类
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 对于登录login接口 toLogin登录页面 注册register 验证码captchaImage 允许匿名访问
                .antMatchers("/login", "/register","/toLogin", "/captchaImage").permitAll()
                // 静态资源，可匿名访问
                .antMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**","**/*.json").permitAll()
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated() //权限通过接口的注解来控制
                .and()
                .headers().frameOptions().disable();
        // 添加Logout filter
        httpSecurity.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        // 添加JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
        httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);

    }

    // （授权）配置 URL 访问权限,对应用户的权限
   /*  @Override
   protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();//开启运行iframe嵌套页面

        //任何请求都必须经过身份验证
        http.authorizeRequests();
                //.anyRequest().authenticated();//任何请求都必须经过身份验证
//                .antMatchers("/vip/vip0/**").hasRole("vip0")//vip1具有的权限：只有vip1用户才可以访问包含url路径"/vip/vip0/**"
//                .antMatchers("/vip/vip1/**").hasRole("vip1")//vip1具有的权限：只有vip1用户才可以访问包含url路径"/vip/vip1/**"
//                .antMatchers("/vip/vip2/**").hasRole("vip2")//vip2具有的权限：只有vip2用户才可以访问url路径"/vip/vip2/**"
//                .antMatchers("/vip/vip3/**").hasRole("vip3");//vip3具有的权限：只有vip3用户才可以访问url路径"/vip/vip3/**"


        //开启表单验证
        http.formLogin()
                .and()
                //.formLogin()//开启表单验证
               // .loginPage("/toLogin")//跳转到自定义的登录页面
                //这里配置的用户名和密码参数需和自定义表单里面的name 相同
               *//* .usernameParameter("username")//自定义表单的用户名的name,默认为username
                .passwordParameter("password")//自定义表单的密码的name,默认为password
                .loginProcessingUrl("/doLogin")//表单请求的地址，一般与form的action属性一致，注意：不用自己写doLogin接口，只要与form的action属性一致即可
                //.successForwardUrl("/index")//登录成功后跳转的页面（重定向）， 报405错误
                .defaultSuccessUrl("/index")//登录成功后跳转的页面（转发）
                .failureForwardUrl("/toLogin")//登录失败后跳转的页面（重定向）
               *//*
               // .and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .logout()//开启注销功能
                .logoutSuccessUrl("/toLogin")//注销后跳转到哪一个页面
                .logoutUrl("/logout") // 配置注销登录请求URL为"/logout"（默认也就是 /logout）
                .clearAuthentication(true) // 清除身份认证信息
                .invalidateHttpSession(true) //使Http会话无效
                .permitAll() // 允许访问登录表单、登录接口
                .and().csrf().disable(); // 关闭csrf
        // 定义登录成功的处理逻辑（可以跳转到某一个页面，也可以返会一段 JSON）
        http.formLogin()
                *//*.successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req,
                                                        HttpServletResponse resp,
                                                        Authentication auth)
                            throws IOException, ServletException {
                        // 我们可以跳转到指定页面
                        // resp.sendRedirect("/index");

                        // 也可以返回一段JSON提示
                        // 获取当前登录用户的信息，在登录成功后，将当前登录用户的信息一起返回给客户端
                        Object principal = auth.getPrincipal();
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        resp.setStatus(200);
                        Map<String, Object> map = new HashMap<>();
                        map.put("status", 200);
                        map.put("msg", principal);
                        ObjectMapper om = new ObjectMapper();
                        out.write(om.writeValueAsString(map));
                        out.flush();
                        out.close();
                    }
                })*//*
                // 定义登录失败的处理逻辑（可以跳转到某一个页面，也可以返会一段 JSON）
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest req,
                                                        HttpServletResponse resp,
                                                        AuthenticationException e)
                            throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        resp.setStatus(401);
                        Map<String, Object> map = new HashMap<>();
                        // 通过异常参数可以获取登录失败的原因，进而给用户一个明确的提示。
                        map.put("status", 401);
                        if (e instanceof LockedException) {
                            map.put("msg", "账户被锁定，登录失败!");
                        }else if(e instanceof BadCredentialsException){
                            map.put("msg","账户名或密码输入错误，登录失败!");
                        }else if(e instanceof DisabledException){
                            map.put("msg","账户被禁用，登录失败!");
                        }else if(e instanceof AccountExpiredException){
                            map.put("msg","账户已过期，登录失败!");
                        }else if(e instanceof CredentialsExpiredException){
                            map.put("msg","密码已过期，登录失败!");
                        }else{
                            map.put("msg","登录失败!");
                        }
                        ObjectMapper mapper = new ObjectMapper();
                        out.write(mapper.writeValueAsString(map));
                        out.flush();
                        out.close();
                    }
                });
    }*/
}
