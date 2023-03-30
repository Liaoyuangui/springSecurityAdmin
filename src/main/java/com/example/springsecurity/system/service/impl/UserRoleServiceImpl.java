package com.example.springsecurity.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.system.dao.UserRoleDao;
import com.example.springsecurity.system.entity.UserRole;
import com.example.springsecurity.system.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * (UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-03-23 12:30:19
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements UserRoleService {

    @Resource
    UserRoleDao userRoleDao;

    @Override
    public Ret queryListByUserId(String userId) {
        List<Map<String, String>> maps = userRoleDao.queryListByUserId(userId);
        return Ret.success(maps);
    }

    @Override
    @Transactional
    public Ret saveUserRole(String userId, List<String> roleList) {
        //先删除用户原来分配的数据
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        this.remove(queryWrapper);
        //保存
        List<UserRole> list = new ArrayList<>();
        for (int i = 0; i < roleList.size(); i++) {
            UserRole ur = new UserRole();
            ur.setId("");
            ur.setUserId(userId);
            ur.setRoleId(roleList.get(i));
            list.add(ur);
        }
        //userRoleDao.insertBatch(list); //不会自动生成id
        this.saveBatch(list,list.size());
        return Ret.success("保存成功！");
    }
}

