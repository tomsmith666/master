package com.blog.controller;

import com.blog.doman.ResponseResult;
import com.blog.doman.entity.Category;
import com.blog.doman.entity.Link;
import com.blog.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    LinkService linkService;
    //分页查询友链列表
    @GetMapping("list")
    public ResponseResult searchLinkList(Integer pageNum,Integer pageSize,String name,String status){
        return linkService.searchLinkList(pageNum,pageSize,name,status);
    }

    //新增友链
    @PostMapping
    public ResponseResult addLinkList(@RequestBody Link link){
        return linkService.addLinkList(link);
    }

    //通过id查询友联(回显)
    @GetMapping("{id}")
    public ResponseResult getLinkList(@PathVariable Long id){
        return linkService.getLinkList(id);
    }


    //按下确定修改友链
    @PutMapping
    public ResponseResult clickUpdateLinkList(@RequestBody Link link){
        return linkService.clickUpdateLinkList(link);
    }

    //通过id删除某个友链
    @DeleteMapping("{id}")
    public ResponseResult deleteLinkList(@PathVariable Long id){
        return linkService.deleteLinkList(id);
    }



}
