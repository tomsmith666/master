package com.blog.controller;

import com.blog.doman.ResponseResult;
import com.blog.doman.dto.RoleDto;
import com.blog.service.MenuService;
import com.blog.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    //展示角色列表
    @GetMapping("list")
    public ResponseResult RoleList(Integer pageNum, Integer pageSize,String roleName,String status){
        return roleService.RoleList(pageNum,pageSize,roleName,status);
    }

    //改变角色状态(statue)
    @PutMapping("changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleDto roleDto){
        return roleService.changeStatus(roleDto);
    }

//    点击按钮新增角色
    @PostMapping
    public ResponseResult clickaddRole(@RequestBody RoleDto roleDto){
        return roleService.clickaddRole(roleDto);
    }

    //修改角色(回显部分)
    @GetMapping("{id}")
    public ResponseResult updateRoleById(@PathVariable Long id){
        return roleService.updateRoleById(id);
    }

    //按下确定更新角色信息
    @PutMapping
    public ResponseResult clickUpdateRole(@RequestBody RoleDto roleDto){
        return roleService.clickUpdateRole(roleDto);
    }

    //通过id删除角色
    @DeleteMapping("{id}")
    public ResponseResult deleteRoleById(@PathVariable Long id){
        return roleService.deleteRoleById(id);
    }

    //新增用户
    @GetMapping("listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }


}
