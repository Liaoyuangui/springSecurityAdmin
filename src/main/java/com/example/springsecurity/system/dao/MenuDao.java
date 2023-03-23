package com.example.springsecurity.system.dao;

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.example.springsecurity.system.entity.Menu;

/**
 * (Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-23 13:09:15
 */
@Mapper
public interface MenuDao extends BaseMapper<Menu> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Menu> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Menu> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Menu> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Menu> entities);

    /**
     * @Description  根据用户查询菜单权限标识
     * @Author liaoyuangui
     * @Date 2023/3/14 15:52
     * @param userId
     * @return java.util.Set<java.lang.String>
     **/
    Set<String> selectMenuPermsByUserId(Integer userId);

    /**
     * @Description  根据用户id 查询菜单列表
     * @Author liaoyuangui
     * @Date 2023/3/15 9:58
     * @param
     * @param userId
     * @return java.util.List<com.example.springsecurity.system.entity.Menu>
     **/
    List<Menu> selectMenuList(Integer userId);
}

