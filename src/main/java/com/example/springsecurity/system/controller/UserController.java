package com.example.springsecurity.system.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springsecurity.common.controller.BaseController;
import com.example.springsecurity.common.utils.JsonUtils;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.system.entity.User;
import com.example.springsecurity.system.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2023-03-23 12:49:36
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * @Description 用户管理页面
     * @Author liaoyuangui
     * @param
     * @return org.springframework.web.servlet.ModelAndView
     **/
    @GetMapping("/indexView")
    @PreAuthorize("@ss.hasPermi('system:user:view')")
    public ModelAndView indexView(){
        return new ModelAndView("pages/system/user/user_list");
    }


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public User selectOne(Integer id) {
        return this.userService.getById(id);
    }

    /**
     * @Description  分页查询查询用户列表
     * @Author liaoyuangui
     * @param
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    @PostMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    public Ret list(@RequestBody Map<String,Object> param){
        Page page = getPageParam(param);
        if(null == page){
            return pageError();
        }
        return userService.getList(page,param);
    }

    /**
     * @Description  删除用户
     * @Author liaoyuangui
     * @param userIds
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('system:user:delete')")
    public Ret delete(@RequestBody String userIds){
        if(StringUtils.isEmpty(userIds)){
            return error("请选择删除的数据！");
        }
        String ids = JsonUtils.getString(userIds, "userIds"); //1,2,3 这种格式
        if(StringUtils.isEmpty(ids)){
            return error("请选择删除的数据！");
        }
        boolean delete = userService.delete(ids);
        if(delete){
            return success("删除成功!");
        }
        return success("删除失败!");
    }

    /**
     * @Description  添加用户
     * @Author liaoyuangui
     * @param user
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    public Ret add(@RequestBody User user){
        return userService.addOrUpdateUser(user);
    }


    /**
     * @Description 修改用户
     * @Author liaoyuangui
     * @param user
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermi('system:user:update')")
    public Ret update(@RequestBody User user){
        return userService.addOrUpdateUser(user);
    }
}

