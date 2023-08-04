package com.blog.controller;

import com.blog.doman.ResponseResult;
import com.blog.doman.entity.Menu;
import com.blog.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    MenuService menuService;
    //展示菜单列表
    @GetMapping("list")
    public ResponseResult MenuList(String status,String menuName){
       return menuService.MenuList(status,menuName);
    }

    //新增菜单
    @PostMapping
    public ResponseResult addmenu(@RequestBody Menu menu){
        return menuService.addnewmenu(menu);
    }

    //通过id修改菜单(回显)
    @GetMapping("{id}")
    public ResponseResult updatemenuById(@PathVariable Long id){
        return menuService.updatemenuById(id);
    }

    //点击按钮更新菜单
    @PutMapping
    public ResponseResult clickUpdeteMenu(@RequestBody Menu menu){
        return menuService.clickUpdeteMenu(menu);
    }

    //通过id删除菜单
    @DeleteMapping("{menuId}")
    public ResponseResult deleteMenuById(@PathVariable Long menuId){
        return menuService.deleteMenuById(menuId);
    }

    //新增角色(能直接设置角色所关联的菜单权限)
    @GetMapping("treeselect")
    public ResponseResult treeselect(){
        return menuService.treeselect();
    }

    //加载角色菜单列表树接口(列表回显)
    @GetMapping("roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable Long id){
        return menuService.roleMenuTreeselect(id);
    }
}
