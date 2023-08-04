package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.doman.entity.Tag;

import java.util.List;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-11 18:37:19
 */
public interface TagMapper extends BaseMapper<Tag> {

    List<Long> selectTagByArticleId(Long id);
}

