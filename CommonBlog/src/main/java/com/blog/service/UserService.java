package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.doman.ResponseResult;
import com.blog.doman.dto.UserDto;
import com.blog.doman.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-07-05 20:38:07
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult uploadUser(User user);

    ResponseResult register(User user);

    //展示用户列表
    ResponseResult showlist(Integer pageNum,Integer pageSize,String userName, String phonenumber, String status);

    //点击确定新增用户
    ResponseResult clickAddUser(UserDto userdto);

    //通过id删除用户
    ResponseResult deleteUserById(Long id);

    //  修改用户(回显byId)
    ResponseResult updateUserById(Long id);

    //更新用户信息
    ResponseResult clickupdateUser(UserDto userDto);
}

