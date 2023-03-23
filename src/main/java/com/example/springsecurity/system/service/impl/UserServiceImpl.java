package com.example.springsecurity.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecurity.common.utils.Res.AjaxResult;
import com.example.springsecurity.common.utils.SecurityUtils;
import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.system.dao.UserDao;
import com.example.springsecurity.system.entity.User;
import com.example.springsecurity.system.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-23 12:49:36
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Resource
    UserDao userDao;

    @Override
    public List<User> getList(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username",user.getUsername());
        queryWrapper.like("nick_name",user.getNickName());
        return userDao.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public boolean delete(String ids) {
        String[] split = ids.split(",");
        List<String> list = Arrays.asList(split);
        int res = 0;
        for(String id : list){
            res = userDao.deleteById(Integer.parseInt(id));
        }
        return res > 0 ? true : false;
    }

    @Override
    public AjaxResult addUser(User user) {
        //校验用户名是否存在了
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",user.getUsername());
        List<User> list = userDao.selectList(queryWrapper);
        if(list.size() > 0){
            return AjaxResult.error("账号已存在，请重新输入！");
        }
        String password = user.getPassword(); //原密码
        String encryptPassword = SecurityUtils.encryptPassword(password);//加密
        user.setPassword(encryptPassword);
        int insert = userDao.insert(user);
        if(insert > 0){
            return AjaxResult.success("添加成功！");
        }
        return AjaxResult.error("添加失败，稍后重试！");
    }

    @Override
    public AjaxResult addOrUpdateUser(User user) {
        if(StringUtils.isEmpty(String.valueOf(user.getId()))){
            return addUser(user);
        }
        //校验用户名是否存在了
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",user.getUsername());
        List<User> list = userDao.selectList(queryWrapper);
        if(list.size() > 1){
            return AjaxResult.error("账号已存在，请重新输入！");
        }
        int update = userDao.updateById(user);
        if(update > 0){
            return AjaxResult.success("修改成功！");
        }
        return AjaxResult.error("修改失败，稍后重试！");
    }
}

