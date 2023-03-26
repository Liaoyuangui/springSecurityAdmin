package com.example.springsecurity.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.system.dao.MenuDao;
import com.example.springsecurity.system.entity.Menu;
import com.example.springsecurity.system.entity.User;
import com.example.springsecurity.system.service.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-03-23 13:09:15
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {

    @Resource
    MenuDao menuDao;

    @Override
    public Set<String> selectMenuPermsByUserId(String userId) {
        return menuDao.selectMenuPermsByUserId(userId);
    }

    @Override
    public List<Menu> selectMenuList(Menu menu, String userId) {
        return menuDao.selectMenuList(userId);
    }

    @Override
    public List<Map<String, Object>> selectMenuList(String userId) {
        List<Menu> menus = menuDao.selectMenuList(userId);
        //找出type为C 的
        //List<Menu> list = menus.stream().filter(a -> "C".equals(a.getMenuType())).collect(Collectors.toList());
        LinkedHashMap<String, List<Menu>> collect = menus.stream().collect(Collectors.groupingBy(Menu::getMenuType, LinkedHashMap::new, Collectors.toList()));
        List<Map<String,Object>> menuList = new ArrayList<>();
        for (String menuType : collect.keySet()){
            if(menuType.equals("M")){
                List<Menu> menuByTypeList = collect.get(menuType);
                for(Menu m : menuByTypeList){
                    //根目录
                    Map<String,Object> map = new HashMap<>();
                    map.put("title",m.getMenuName());
                    map.put("icon",m.getIcon());
                    map.put("href",m.getPath());
                    map.put("id",m.getId().toString());
                    String id = m.getId();
                    List<Map<String,String>> childList = new ArrayList<>();
                    //找下面的子菜单
                    List<Menu> itemList = menus.stream().filter(a -> a.getParentId().equals(id)).collect(Collectors.toList());
                    for(Menu itemMenu : itemList){
                        Map<String,String> itemMap = new HashMap<>();
                        itemMap.put("title",itemMenu.getMenuName());
                        itemMap.put("icon",itemMenu.getIcon());
                        itemMap.put("href",itemMenu.getPath());
                        itemMap.put("id",itemMenu.getId().toString());
                        childList.add(itemMap);
                    }
                    map.put("children",childList);
                    menuList.add(map);
                }

            }
        }
        return menuList;
    }

    @Override
    public Ret getList(Page page, Map<String, Object> param) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("menu_name",param.get("menuName"));
        queryWrapper.like("menu_type",param.get("menuType"));
        queryWrapper.orderByAsc("order_num");
        Page<Menu> dataPage = menuDao.selectPage(page, queryWrapper);
        List<Menu> records = dataPage.getRecords();
        List<Menu> dataList = records;
        //添加父级目录，数据多建议直接用sql处理
        records.forEach(a->{
            String parentId = a.getParentId();
            if(StringUtils.isNotEmpty(parentId)){
                Optional<Menu> first = dataList.stream().filter(e -> parentId.equals(e.getId())).findFirst();
                if(first.isPresent()){
                    a.setParentName(first.get().getMenuName());
                }
            }
        });
        dataPage.setRecords(records);
        return Ret.getPageOkResult(dataPage);
    }
}

