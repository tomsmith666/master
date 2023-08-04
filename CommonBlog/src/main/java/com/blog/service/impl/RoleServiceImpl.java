package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.doman.ResponseResult;
import com.blog.doman.dto.RoleDto;
import com.blog.doman.entity.Role;
import com.blog.doman.entity.RoleMenu;
import com.blog.doman.vo.PageVo;
import com.blog.mapper.RoleMapper;
import com.blog.mapper.RoleMenuMapper;
import com.blog.service.RoleMenuService;
import com.blog.service.RoleService;
import com.blog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-07-13 11:51:31
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> getRoleKeyByUserId(Long id) {
        //如果是超级管理员(id=1),相应的roles部分直接是admin就行
        if(id == 1L){
            ArrayList<String> strings = new ArrayList<>();
            strings.add("admin");
            return strings;
        }
        //不是超级管理员，就要进行mybatis的select筛选，拿到最终要响应的roles(List<String>)
        return getBaseMapper().getRoleKeyByUserId(id);
    }

    //展示角色列表
    @Override
    public ResponseResult RoleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        roleLambdaQueryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        roleLambdaQueryWrapper.orderByAsc(Role::getRoleSort);

        //        Page<Role> page = new Page<>();
        //        page.setCurrent(pageNum);
        //        page.setSize(pageSize);   或者写成下面这种写法
        Page<Role> page = new Page<>(pageNum,pageSize);

        page(page,roleLambdaQueryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    //改变角色状态(statue)
    @Autowired
    RoleMapper roleMapper;
    @Override
    public ResponseResult changeStatus(RoleDto roleDto) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getId,roleDto.getRoleId());
        Role role = new Role();
        role.setStatus(roleDto.getStatus());
        roleMapper.update(role,roleLambdaQueryWrapper);
        return ResponseResult.okResult();
    }

    //点击按钮新增角色
    @Autowired
    RoleMenuService roleMenuService;
    @Override
    public ResponseResult clickaddRole(RoleDto roleDto) {
        //将新增用户插入表中
        Role role1 = BeanCopyUtils.copyBean(roleDto, Role.class);
        roleMapper.insert(role1);

        //但请求体的menuid属性没有添加到sys_role_menu表上，需要进行添加
        List<Long> menuIds = roleDto.getMenuIds();
        List<RoleMenu> roleMenuList = menuIds.stream()
                .map(menuid -> new RoleMenu(role1.getId(), menuid))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
        return ResponseResult.okResult();
    }

    //修改角色(回显部分)
    @Override
    public ResponseResult updateRoleById(Long id) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getId,id);
        Role role = roleMapper.selectOne(roleLambdaQueryWrapper);
        return ResponseResult.okResult(role);
    }

    //按下确定更新角色信息
    @Autowired
    RoleMenuMapper roleMenuMapper;
    @Override
    public ResponseResult clickUpdateRole(RoleDto roleDto) {
        //更新角色信息
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getId,role.getId());
        roleMapper.update(role,roleLambdaQueryWrapper);


        LambdaQueryWrapper<RoleMenu> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,roleDto.getId());
        roleMenuMapper.delete(queryWrapper);

        //但请求体的menuid属性没有添加到sys_role_menu表上，需要进行添加
        List<Long> menuIds = roleDto.getMenuIds();
        List<RoleMenu> roleMenuList = menuIds.stream()
                .map(menuid -> new RoleMenu(role.getId(), menuid))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
        return ResponseResult.okResult();
    }

    //通过id删除角色
    @Override
    public ResponseResult deleteRoleById(Long id) {
        roleMapper.deleteById(id);
        return ResponseResult.okResult();
    }

    //新增用户

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getStatus,"0");
        List<Role> list = list(roleLambdaQueryWrapper);
        return ResponseResult.okResult(list);
    }
}
