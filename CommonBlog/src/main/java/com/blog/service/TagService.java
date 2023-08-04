package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.doman.ResponseResult;
import com.blog.doman.dto.TagListDto;
import com.blog.doman.entity.Tag;
import com.blog.doman.vo.PageVo;
import com.blog.doman.vo.TagVo;
import com.blog.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-07-11 18:37:20
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

//    新增标签
    ResponseResult add(TagListDto tagListDto);

    //修改标签
    ResponseResult put(TagListDto tagListDto);

    //    写博客查询所有标签接口
    List<TagVo> listAllTag();
}

