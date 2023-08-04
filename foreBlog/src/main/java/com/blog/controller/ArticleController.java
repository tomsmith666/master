package com.blog.controller;
import com.blog.doman.ResponseResult;
import com.blog.doman.entity.Article;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;

//    @GetMapping("/test")
//    public List<Article> test(){
//        return articleService.list();
//    }

//    获得右侧热门书的list数组
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        ResponseResult result = articleService.gethotArticleList();
        return result;
    }

    @GetMapping("articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id")Long id){

        return articleService.getArticleDetail(id);
    }

    //更新浏览量(更新的是Redis的数据，会定期放到数据库里面)
    @PutMapping("updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id")Long id){
        return articleService.updateViewCount(id);
    }
}
