package com.blog.controller;

import com.blog.doman.ResponseResult;
import com.blog.doman.dto.AddArticleDto;
import com.blog.doman.entity.Menu;
import com.blog.doman.vo.ArticleListParmer;
import com.blog.service.ArticleService;
import com.blog.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    //写博文，接收AddArticleDto的实例请求体
    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        return articleService.add(articleDto);
    }

    //后台文章列表显示
    @GetMapping("list")
    public ResponseResult list(Integer pageNum, Integer pageSize,String title ,String summary){
        return articleService.pageArticlreList(pageNum,pageSize,title,summary);
    }
    //查询文章详情接口
    @GetMapping("{id}")
    public ResponseResult getInfoById(@PathVariable Long id){
        return articleService.getInfoById(id);
    }
    //点击后更新文章接口
    @PutMapping
    public ResponseResult articleput(@RequestBody ArticleListParmer articleListParmer){
        return articleService.updateArticle(articleListParmer);
    }
    //通过id删除文章
    @DeleteMapping("{id}")
    public ResponseResult deleteArticleById(@PathVariable Long id){
        return articleService.deleteArticleById(id);
    }



}
