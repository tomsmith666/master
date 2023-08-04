package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.doman.ResponseResult;
import com.blog.doman.dto.AddArticleDto;
import com.blog.doman.entity.Article;
import com.blog.doman.vo.ArticleListParmer;

import java.util.List;

public interface ArticleService extends IService<Article> {
    ResponseResult gethotArticleList();


    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto articleDto);
    //后台文章列表显示
    ResponseResult pageArticlreList(Integer pageNum, Integer pageSize, String title, String summary);
    //查询文章详情接口
    ResponseResult getInfoById(Long id);
    //点击后更新文章接口
    ResponseResult updateArticle(ArticleListParmer articleListParmer);
    //通过id删除文章
    ResponseResult deleteArticleById(Long id);

}
