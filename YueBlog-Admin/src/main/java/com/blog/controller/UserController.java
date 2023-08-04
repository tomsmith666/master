package com.blog.controller;

import com.blog.doman.ResponseResult;
import com.blog.doman.dto.UserDto;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    UserService userService;
    //展示用户列表
    @GetMapping("list")
    public ResponseResult showlist(Integer pageNum,Integer pageSize, String userName,String phonenumber,String status){
        return userService.showlist(pageNum,pageSize,userName,phonenumber,status);
    }

    //点击确定新增用户
    @PostMapping
    public ResponseResult clickAddUser(@RequestBody UserDto userdto){
        return userService.clickAddUser(userdto);
    }

    //通过id删除用户
    @DeleteMapping("{id}")
    public ResponseResult deleteUserById(@PathVariable Long id){
        return userService.deleteUserById(id);
    }

    //修改用户(回显byId)  有问题
    @GetMapping("{id}")
    public ResponseResult updateUserById(@PathVariable Long id){
        return userService.updateUserById(id);
    }

//    更新用户信息
    @PutMapping
    public ResponseResult clickupdateUser(@RequestBody UserDto userDto){
        return userService.clickupdateUser(userDto);
    }

}
