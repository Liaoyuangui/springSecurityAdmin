package com.example.springsecurity.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springsecurity.common.controller.BaseController;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.system.entity.Menu;
import com.example.springsecurity.system.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * (Menu)表控制层
 *
 * @author makejava
 * @since 2023-03-23 13:09:15
 */
@RestController
@RequestMapping("system/menu")
public class MenuController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private MenuService menuService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param menu 查询实体
     * @return 所有数据
     */
    @GetMapping
    public Ret selectAll(Page<Menu> page, Menu menu) {
        return success(this.menuService.page(page, new QueryWrapper<>(menu)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public Ret selectOne(@PathVariable Serializable id) {
        return success(this.menuService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param menu 实体对象
     * @return 新增结果
     */
    @PostMapping
    public Ret insert(@RequestBody Menu menu) {
        return success(this.menuService.save(menu));
    }

    /**
     * 修改数据
     *
     * @param menu 实体对象
     * @return 修改结果
     */
    @PutMapping
    public Ret update(@RequestBody Menu menu) {
        return success(this.menuService.updateById(menu));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public Ret delete(@RequestParam("idList") List<Long> idList) {
        return success(this.menuService.removeByIds(idList));
    }



    /**
     * 获取菜单列表
     */
    //@PreAuthorize("hasAuthority('system:menu:list')")
    @PreAuthorize("hasAnyAuthority('system:menu:list')")
    //@PreAuthorize("@ss.hasPermi('system:menu:list')")
    //@PreAuthorize("hasAnyRole('vip1')")
    @GetMapping("/list")
    public Ret list(){
        List<Map<String, Object>> menus = menuService.selectMenuList(getUserId());
        return success(menus);
    }

}

