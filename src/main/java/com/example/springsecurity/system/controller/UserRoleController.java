package com.example.springsecurity.system.controller;


import com.example.springsecurity.common.controller.BaseController;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.system.service.UserRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * (UserRole)表控制层
 *
 * @author makejava
 * @since 2023-03-23 12:30:19
 */
@RestController
@RequestMapping("system/userRole")
public class UserRoleController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private UserRoleService userRoleService;

    /***
     * 视图
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:userRole:list')")
    @GetMapping("/indexView")
    public ModelAndView indexView(){
        return new ModelAndView("pages/system/userRole/user_role_list");
    }

    /**
     * @Description 查询人员有哪些角色
     * @Author liaoyuangui
     * @param
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    @GetMapping("/queryListByUserId")
    public Ret queryListByUserId(@RequestParam("userId") String userId){
        if(StringUtils.isEmpty(userId)){
            return Ret.error("用户编号不能为空！");
        }
        return userRoleService.queryListByUserId(userId);
    }

    /**
     * @Description  保存权限
     * @Author liaoyuangui
     * @Date 2023/3/30 14:32
     * @param userRole
     * @return com.example.springsecurity.common.utils.Res.Ret
     **/
    @PostMapping("/saveUserRole")
   // @PreAuthorize("@ss.hasPermi('system:userRole:save')")
    public Ret saveUserRole(@RequestBody Map<String,Object> userRole){
        Object userId = userRole.get("userId");
        if(null == userId){
            return Ret.error("授权失败，未获取到用户信息");
        }
        List<String> roleList = (List<String>) userRole.get("role");
        return  userRoleService.saveUserRole(userId.toString(),roleList);
    }

}

