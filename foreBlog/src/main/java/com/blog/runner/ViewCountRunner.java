package com.blog.runner;

import com.blog.doman.entity.Article;
import com.blog.mapper.ArticleMapper;
import com.blog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
//继承CommandLineRunner接口，在应用启动时把博客的浏览量存储到redis中
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        //查到 Article表里所有信息
        List<Article> articles = articleMapper.selectList(null);
       Map<String,Integer> map= articles.stream()
                .collect(Collectors.toMap(article->article.getId().toString(),article->{
                   return article.getViewCount().intValue();
                }));
        redisCache.setCacheMap("ArticleViewCount",map);
    }
}
