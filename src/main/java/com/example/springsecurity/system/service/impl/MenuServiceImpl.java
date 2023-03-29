package com.example.springsecurity.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecurity.common.utils.Res.Ret;
import com.example.springsecurity.common.utils.StringUtils;
import com.example.springsecurity.system.dao.MenuDao;
import com.example.springsecurity.system.dao.UserDao;
import com.example.springsecurity.system.entity.Menu;
import com.example.springsecurity.system.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Menu> selectMenuByUserId(String userId){
        List<Menu> menus = menuDao.selectMenuList(userId);
        return  buildMenuList(menus);
    }


    @Override
    public Ret getMenuList(Page page, Map<String, Object> param) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("menu_name",param.get("menuName"));
        queryWrapper.like("menu_type",param.get("menuType"));
        queryWrapper.orderByAsc("order_num");
        //查询所有的菜单数据
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
        List<Menu> menus = buildMenuList(records);
        dataPage.setRecords(menus);
        return Ret.getPageOkResult(dataPage);
    }

    @Override
    public List<Menu> queryAllMenuNotButton() {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("menu_type","F"); //不包含按钮
        queryWrapper.orderByAsc("order_num");
        List<Menu> menus = menuDao.selectList(queryWrapper);
        return buildMenuList(menus);
    }

    @Override
    @Transactional
    public Ret delete(String idList) {
        if(StringUtils.isEmpty(idList)){
            return Ret.error("请选择数据进行删除！");
        }
        String[] split = idList.split(",");
        List<String> list = Arrays.asList(split);
        for(String id : list){
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id",id);
            List<Menu> menus = menuDao.selectList(queryWrapper);
            if(menus.size() > 0){
                return  Ret.error("选择的菜单含有子节点，不能删除，请先删掉子菜单！");
            }
        }
        int i = menuDao.deleteBatchIds(list);
        if(i > 0){
            return Ret.success("删除成功!");
        }
        return Ret.error("删除失败！");
    }

    /***
     * 组装菜单结构
     * @param menuList 所有的菜单数据
     * @return
     */
    private List<Menu> buildMenuList(List<Menu> menuList){
        List<Menu> finalNode = new ArrayList<>();
        //根目录
        List<Menu> collect = menuList.stream().filter(a -> "M".equals(a.getMenuType())).collect(Collectors.toList());
        for(Menu menu : collect){
            finalNode.add(selectChildren(menu,menuList));
        }
        return finalNode;
    }

    /***
     * 查找子节点
     * 一层里面有二层，二层里面有三层...
     * @return
     */
    private Menu selectChildren(Menu menu,List<Menu> menuList){
        menu.setChildren(new ArrayList<>());
        //根据id 和 parentId 获取到这个主菜单下的所有子菜单
        List<Menu> collect = menuList.stream().filter(a -> menu.getId().equals(a.getParentId())).collect(Collectors.toList());
        if(collect.size() > 0){
            for(Menu m : collect){
                List<Menu> children = menu.getChildren();
                if(null == menu.getChildren()){
                    menu.setChildren(new ArrayList<>());
                }
                children.add(selectChildren(m,menuList)); //这里使用递归算法实现
            }
        }
        return menu;
    }
}

