package com.example.springsecurity.security.entity;

import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.system.entity.Role;
import com.example.springsecurity.system.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Auther: Liaoyuangui
 * @Date: 2023/3/8 09:40
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails, Serializable {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户登录成功生成的唯一标识
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    //用户信息
    private User user;

    public LoginUser(User user,Set<String> permissions){
        this.user = user;
        this.userId = user.getId();
        this.permissions = permissions;
    }

    /**
     * 返回用户的权限集合。
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        /*for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleKey()));
        }*/
        for(String permission : permissions){
            if(StringUtils.isNotEmpty(permission)){
                authorities.add(new SimpleGrantedAuthority(permission));
            }
        }
        return authorities;
    }

    /**
     * 返回账号的密码
     * @return
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 返回账号的用户名
     * @return
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 账号是否失效，true:账号有效，false账号失效。
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号是否被锁，true:账号没被锁，可用；false：账号被锁，不可用
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 账号认证是否过期，true:没过期，可用；false：过期，不可用
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账号是否可用，true:可用，false:不可用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


}
