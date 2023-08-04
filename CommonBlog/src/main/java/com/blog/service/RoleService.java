package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.doman.ResponseResult;
import com.blog.doman.dto.RoleDto;
import com.blog.doman.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-07-13 11:51:31
 */
public interface RoleService extends IService<Role> {

    List<String> getRoleKeyByUserId(Long id);
    //展示角色列表
    ResponseResult RoleList(Integer pageNum, Integer pageSize, String roleName, String status);

    //改变角色状态(statue)
    ResponseResult changeStatus(RoleDto roleDto);

    //点击按钮新增角色
    ResponseResult clickaddRole(RoleDto roleDto);

    //修改角色(回显部分)
    ResponseResult updateRoleById(Long id);

    //按下确定更新角色信息
    ResponseResult clickUpdateRole(RoleDto roleDto);

    //通过id删除角色
    ResponseResult deleteRoleById(Long id);

    //新增用户
    ResponseResult listAllRole();
}

