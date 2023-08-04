package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.doman.ResponseResult;
import com.blog.doman.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-07-13 11:53:34
 */
public interface MenuService extends IService<Menu> {

    List<String> getParamsByUserId(Long id);
    List<Menu> selectRouterMenuTreeByUserId(Long userId);
    //展示菜单列表
    ResponseResult MenuList(String status, String menuName);
    //新增菜单
    ResponseResult addnewmenu(Menu menu);

    //通过id修改菜单(回显)
    ResponseResult updatemenuById(Long id);

    //点击按钮更新菜单
    ResponseResult clickUpdeteMenu(Menu menu);

    //通过id删除菜单
    ResponseResult deleteMenuById(Long menuId);

    //新增角色(能直接设置角色所关联的菜单权限)
    ResponseResult treeselect();

    //加载角色菜单列表树接口(列表回显)
    ResponseResult roleMenuTreeselect(Long id);
}

