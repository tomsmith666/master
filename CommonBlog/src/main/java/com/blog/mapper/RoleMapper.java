package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.doman.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-13 11:51:29
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<String> getRoleKeyByUserId(Long id);
}

