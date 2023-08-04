package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.doman.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-13 11:53:32
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> getParamsByUserId(Long id);
    List<Menu> selectAllRouterMenu();
    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> selectAllRouterMenuTwo();

    List<Long> getcheckedKeys();
}

