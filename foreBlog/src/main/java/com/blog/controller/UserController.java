package com.blog.controller;

import com.blog.annotation.SystemLog;
import com.blog.doman.ResponseResult;
import com.blog.doman.entity.User;
import com.blog.service.LinkService;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/userInfo")
//    前端用请求地址直接穿过来用户id，可能会有冒充此请求然后获取服务器返回的用户信息风险，
//    所以不用传id参数，运用Holder里村的token来获取对应的用户id
    public ResponseResult userInfo() {
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult uploadUser(@RequestBody User user){
        return userService.uploadUser(user);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}