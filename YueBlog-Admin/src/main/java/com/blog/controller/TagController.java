package com.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.doman.ResponseResult;
import com.blog.doman.dto.TagListDto;
import com.blog.doman.entity.Tag;
import com.blog.doman.vo.PageVo;
import com.blog.doman.vo.TagVo;
import com.blog.mapper.TagMapper;
import com.blog.service.TagService;
import com.blog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

//   查询标签列表接口
//    @GetMapping("/list")
//    public ResponseResult list(){
//        return ResponseResult.okResult(tagService.list());
//    }

//    查询标签列表接口
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    //新增标签
    @PostMapping
    public ResponseResult tag(@RequestBody TagListDto tagListDto){
        return  tagService.add(tagListDto);
    }

    //删除标签
    @DeleteMapping("{id}")
    public ResponseResult deletetag(@PathVariable Long id){
        tagService.removeById(id);
        return ResponseResult.okResult();
    }

    //获取标签信息:点击修改按钮会将 原有修改名和备注信息 呈现到修改窗口上
    @GetMapping("{id}")
    public ResponseResult<TagListDto> getusedtaginfo(@PathVariable Long id){
        Tag tag = tagService.getById(id);
        TagListDto tagListDto = BeanCopyUtils.copyBean(tag, TagListDto.class);
        tagListDto.setId(id);
        return ResponseResult.okResult(tagListDto);

    }

    //修改标签
    @PutMapping
    public ResponseResult updatetag(@RequestBody TagListDto tagListDto){
        return tagService.put(tagListDto);
    }

//    /content/tag/listAllTag
//    写博客查询所有标签接口
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> tagVos = tagService.listAllTag();
        return ResponseResult.okResult(tagVos);
    }
}
