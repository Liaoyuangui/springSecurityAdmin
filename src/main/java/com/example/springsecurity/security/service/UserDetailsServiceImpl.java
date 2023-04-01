package com.example.springsecurity.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springsecurity.security.entity.LoginUser;
import com.example.springsecurity.system.dao.RoleDao;
import com.example.springsecurity.system.dao.UserDao;
import com.example.springsecurity.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.List;
import java.util.Set;

/**
 * 创建服务层（UserService），该层获取数据库数据，将数据交给SpringSecurity做用户的认证与授权，
 * 为此，需要实现接口UserDetailsService，并且实现该接口下的loadUserByUsername方法，
 * 该方法获取数据库数据
 * @Auther: Liaoyuangui
 * @Date: 2023/2/13 09:58
 * @Description:
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserDao userDao;

    @Resource
    private RoleDao roleDao;

    @Resource
    private SysPermissionService permissionService;

    /**
     * @Description  根据用户名获取用户及权限
     * @Author liaoyuangui
     * @Date 2023/2/13 14:51
     * @param username
     * @return org.springframework.security.core.userdetails.UserDetails
     **/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        List<User> users = userDao.selectList(queryWrapper);
        if (null == users || users.size() ==0) {
            throw new UsernameNotFoundException("该用户不存在！");
        }else{
            User user = users.get(0);
            //设置权限信息
            //user.setRoles(roleDao.getRolesByUserId(users.get(0).getId()));
            //权限信息
            Set<String> permission = permissionService.getMenuPermission(user);
            //user -> LoginUser
            LoginUser loginUser = new LoginUser(user,permission);
            System.out.println("******登录用户："+loginUser.getUsername()+"，*****权限列表："+loginUser.getAuthorities());
            return loginUser; //返回用户
        }
    }

}
