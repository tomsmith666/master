package com.blog.controller;

import com.blog.doman.ResponseResult;
import com.blog.doman.entity.LoginUser;
import com.blog.doman.entity.Menu;
import com.blog.doman.entity.User;
import com.blog.doman.vo.AdminUserInfoVo;
import com.blog.doman.vo.RoutersVo;
import com.blog.doman.vo.UserInfoVo;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.exception.SystemException;
import com.blog.service.LoginService;
import com.blog.service.MenuService;
import com.blog.service.RoleService;
import com.blog.utils.BeanCopyUtils;
import com.blog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
//    登录
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

//    获取登陆的权限信息进行页面展示
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息 menu表
        List<String> permissions = menuService.getParamsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息 role表
        List<String> roles = roleService.getRoleKeyByUserId(loginUser.getUser().getId());
        //BeanCopyUtils拿到UserInfoVo类型的user
        // 根据相应格式将permissions，roles，user封装到AdminUserInfoVo里面，ok方法数据返回
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(permissions, roles, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
//    获取登陆的路由信息进行页面展示
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询要呈现在页面上的menu(根据权限)，结果是tree的形式
        // (因为RoutersVo里面是List<Menu>，其中的children属性里也是List<Menu>)，所以要tree形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

//    退出登录接口
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}