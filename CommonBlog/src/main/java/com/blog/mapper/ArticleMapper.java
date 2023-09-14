package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.doman.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ArticleMapper extends BaseMapper<Article> {
    List<String> inputTestId(@Param("id") Long id);
}
