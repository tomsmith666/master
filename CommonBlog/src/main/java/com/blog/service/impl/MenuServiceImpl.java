package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.doman.ResponseResult;
import com.blog.doman.dto.RoleMenuDto;
import com.blog.doman.entity.Menu;
import com.blog.doman.vo.MenuTreeVo;
import com.blog.mapper.MenuMapper;
import com.blog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blog.service.MenuService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-07-13 11:53:34
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> getParamsByUserId(Long id) {
        //如果是超级管理员(id=1)，返回所有的权限(显示menutype为C或F，即菜单或按钮。status为0即未删除的)，
        // 最后只拿到perms字段组成的List<String>
        if (id == 1L) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, "C", "F");
            queryWrapper.eq(Menu::getStatus, "0");
            List<Menu> list = list(queryWrapper);
            List<String> collect = list.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return collect;
        }
        //不是管理员只能进行多表查询，比较复杂不使用mbplus，使用原始的mybatis进行数据库查询
        // 先去sys_user_role表根据userid查到roleid(左连接)，到sys_role_menu表根据roleid查到menuid(左连接)，
        // where进一步筛选查询perms字段的值
        return getBaseMapper().getParamsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        //注意写在这里是为了提升变量作用域
        List<Menu> menus = null;
        if (userId.equals(1L)) {
            //如果是超级管理员(userId为1)
            menus = getBaseMapper().selectAllRouterMenu();
        } else {
            //是其他身份
            menus = getBaseMapper().selectRouterMenuTreeByUserId(userId);
        }
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        return menuTree;
    }




    //展示菜单列表
    @Autowired
    MenuMapper menuMapper;

    @Override
    public ResponseResult MenuList(String status, String menuName) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 可以针对菜单名进行模糊查询
        menuLambdaQueryWrapper.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
        //也可以针对菜单的状态进行查询
        menuLambdaQueryWrapper.eq(StringUtils.hasText(status), Menu::getStatus, status);
        menuLambdaQueryWrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> menuList = list(menuLambdaQueryWrapper);
        return ResponseResult.okResult(menuList);
    }

    //新增菜单
    @Override
    public ResponseResult addnewmenu(Menu menu) {
        menuMapper.insert(menu);
        return ResponseResult.okResult();
    }

    //通过id修改菜单(回显)
    @Override
    public ResponseResult updatemenuById(Long id) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getId, id);
        Menu menu = menuMapper.selectOne(menuLambdaQueryWrapper);
        return ResponseResult.okResult(menu);
    }


    //点击按钮更新菜单
    @Override
    public ResponseResult clickUpdeteMenu(Menu menu) {
        if (!menu.getParentId().equals(menu.getId())) {
            LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            menuLambdaQueryWrapper.eq(Menu::getId, menu.getId());
            menuMapper.update(menu, menuLambdaQueryWrapper);
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(500, "修改菜单'写博文'失败，上级菜单不能选择自己");
        }
    }

    //通过id删除菜单
    @Override
    public ResponseResult deleteMenuById(Long menuId){
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getParentId,menuId);
        List<Menu> menus = menuMapper.selectList(menuLambdaQueryWrapper);
        if(menus.isEmpty()){
            menuMapper.deleteById(menuId);
            return ResponseResult.okResult();
        }
        else {
            return ResponseResult.errorResult(500,"存在子菜单不允许删除");
        }
    }

    //新增角色(能直接设置角色所关联的菜单权限)
    @Override
    public ResponseResult treeselect() {
        List<Menu> menus = null;
        menus = baseMapper.selectAllRouterMenuTwo();
        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        // List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menuTree, MenuTreeVo.class);
        List<MenuTreeVo> menuTreeVoList = new ArrayList<>();
        for (Menu menu : menuTree) {
            MenuTreeVo menuTreeVo = BeanCopyUtils.copyBean(menu, MenuTreeVo.class);
            menuTreeVo.setLabel(menu.getMenuName());
            menuTreeVoList.add(menuTreeVo);
        }
        return ResponseResult.okResult(menuTreeVoList);
    }
    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))  //查询第一层父id为0的目录
                .map(menu -> menu.setChildren(getChildren(menu, menus)))//查询子菜单集合并赋值，menu为第一层父id为0的目录，menus为查询出的全部菜单
                .collect(Collectors.toList());
        return menuTree;
    }
//    查询子菜单集合并赋值，menu为第一层父id为0的目录，menus为查询出的全部菜单
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

    //加载角色菜单列表树接口(列表回显)
    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        //获取菜单树
        List<Menu> menus = null;
        menus = baseMapper.selectAllRouterMenuTwo();
        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        // List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menuTree, MenuTreeVo.class);
        List<MenuTreeVo> menuTreeVoList = new ArrayList<>();
        for (Menu menu : menuTree) {
            MenuTreeVo menuTreeVo = BeanCopyUtils.copyBean(menu, MenuTreeVo.class);
            menuTreeVo.setLabel(menu.getMenuName());
            menuTreeVoList.add(menuTreeVo);
        }

        //获取checkedKeys：角色所关联的菜单权限id列表
        List<Long>checkedKeys= baseMapper.getcheckedKeys();
        RoleMenuDto roleMenuDto = new RoleMenuDto(menuTreeVoList,checkedKeys);
        return ResponseResult.okResult(roleMenuDto);
    }
}
