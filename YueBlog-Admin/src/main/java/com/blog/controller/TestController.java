package com.blog.controller;

import com.blog.doman.ResponseResult;
import com.blog.doman.vo.TagVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    // 注入URL资源
    @Value("http://www.baidu.com")
    private Resource url;
    @GetMapping("/test1")
    public void test1(){
        System.out.println(url);
    }
}
