package com.blog.controller;

import com.blog.doman.ResponseResult;
import com.blog.doman.entity.User;
import com.blog.service.UploadService;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Autowired
   private UploadService uploadService;

    //更新用户头像
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }

}
