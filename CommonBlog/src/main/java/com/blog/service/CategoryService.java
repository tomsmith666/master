package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.doman.ResponseResult;
import com.blog.doman.dto.CategoryDto;
import com.blog.doman.entity.Category;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-07-01 14:23:31
 */
public interface CategoryService extends IService<Category> {
    ResponseResult getCategoryList();
    //    写博客 查询所有分类接口
    List<CategoryDto> listAllCategory();

    //分页查询分类列表
    ResponseResult getcategoryList(Integer pageNum, Integer pageSize, String name, String status);

    //新增分类
    ResponseResult addcategory(Category category);

    //通过id修改分类(回显)
    ResponseResult updatecategoryById(Long id);

    //按下确定更新分类
    ResponseResult clickUpdateCategory(Category category);

    //通过id删除某个分类
    ResponseResult deleteCategoryById(Long id);
}

