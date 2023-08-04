package com.blog.service;

import com.blog.doman.ResponseResult;
import com.blog.doman.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}