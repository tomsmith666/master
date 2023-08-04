package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.constants.SystemConstants;
import com.blog.doman.ResponseResult;
import com.blog.doman.dto.CategoryDto;
import com.blog.doman.entity.Article;
import com.blog.doman.entity.Category;
import com.blog.doman.vo.CategoryVo;
import com.blog.doman.vo.PageVo;
import com.blog.mapper.CategoryMapper;
import com.blog.service.ArticleService;
import com.blog.service.CategoryService;
import com.blog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-07-01 14:23:32
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    ArticleService articleService;
    @Override
    public ResponseResult getCategoryList(){
        //查询文章表articleService，状态为已发布的文章(Status为0代表已发布，为1代表草稿)
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(queryWrapper);
        //获取每个文章的分类id并去重，将他们变成Set集合
        Set<Long> collect = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());           //set会进行自动去重
        //到分类表里面，根据Set集合里面的id，查寻属于哪个类，将涉及到的类存放到List里面
        List<Category> categories = listByIds(collect);
        List<Category> collect1 = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))//保证分类的状态是正常的(Status=0)
                .collect(Collectors.toList());
        //封装vo，用封装好的copyBeanList方法，只服务器只返回给浏览器需要的属性(CategoryVo类里面定义好了需要的属性)
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(collect1, CategoryVo.class);
        //okResult方法，将返回值进一步封装，带上状态码等
        return ResponseResult.okResult(categoryVos);
    }

    //写博客 查询所有分类接口
    @Override
    public List<CategoryDto> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,SystemConstants.STATIC_STATUS_NORMAL);
        List<Category> list = list(queryWrapper);
        List<CategoryDto> categoryDtos = BeanCopyUtils.copyBeanList(list, CategoryDto.class);
        return categoryDtos;
    }

    //分页查询分类列表
    @Override
    public ResponseResult getcategoryList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        categoryLambdaQueryWrapper.eq(StringUtils.hasText(status),Category::getStatus,status);

        Page<Category> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,categoryLambdaQueryWrapper);

        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    //新增分类
    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public ResponseResult addcategory(Category category) {
        categoryMapper.insert(category);
        return ResponseResult.okResult();
    }

    //通过id修改分类(回显)
    @Override
    public ResponseResult updatecategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        return  ResponseResult.okResult(category);
    }

    //按下确定更新分类
    @Override
    public ResponseResult clickUpdateCategory(Category category) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getName,category.getName());
        categoryMapper.update(category,categoryLambdaQueryWrapper);
        return ResponseResult.okResult();
    }

    //通过id删除某个分类
    @Override
    public ResponseResult deleteCategoryById(Long id) {
        categoryMapper.deleteById(id);
        return ResponseResult.okResult();
    }
}
