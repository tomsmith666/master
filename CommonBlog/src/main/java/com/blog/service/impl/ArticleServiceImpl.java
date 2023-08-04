package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.constants.SystemConstants;
import com.blog.doman.ResponseResult;

import com.blog.doman.dto.AddArticleDto;
import com.blog.doman.entity.Article;

import com.blog.doman.entity.ArticleTag;
import com.blog.doman.entity.Category;
import com.blog.doman.entity.Tag;
import com.blog.doman.vo.*;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.ArticleTagMapper;
import com.blog.mapper.TagMapper;
import com.blog.service.ArticleService;

import com.blog.service.ArticleTagService;
import com.blog.service.CategoryService;
import com.blog.utils.BeanCopyUtils;
import com.blog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper,Article> implements ArticleService {
    @Autowired
    @Lazy
    private CategoryService categoryService;
    //写博客
    @Autowired
    @Lazy
    private ArticleTagService articleTagService;
    @Autowired
    RedisCache redisCache;
    @Autowired
    ArticleMapper articleMapper;

    @Override
    public ResponseResult gethotArticleList(){
//        查询热门文章，将其封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        其实是取"Status"字段，但是怕拼写错误，Article::getStatus方法，这里规定0是热门
//        ARTICLE_STATUS_NORMAL就是规定的常量0
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
//        根据浏览量进行降序排列，结果还是数组类型
        queryWrapper.orderByDesc(Article::getViewCount);
//        从第一页开始要10条数据
        Page<Article> page = new Page(1,10);
//       是数组类型的几条数据，但是条数有了限制
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();

//        bean拷贝
//        //创建一个hotArticleVos用来装拷贝的数组
//        ArrayList<HotArticleVo> hotArticleVos = new ArrayList<>();
////        bean拷贝的字段名字前后对象里要一致
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            hotArticleVos.add(vo);
//        }
        
//        用封装好的bean拷贝工具类,实现和上面一样的功能
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
//        拿到ResponseResult的okResult会把这个数组当场元素加入要返回的对象
        return ResponseResult.okResult(hotArticleVos);
    }
    @Override
    public  ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //如果有categoryId，就要查询时和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&& categoryId>0,Article::getCategoryId,categoryId);
        //状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //对idTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(1,10);
        page(page,lambdaQueryWrapper);
        List<Article> articles = page.getRecords();
        //这样返回的categoryName值为NULL，要给categoryName属性赋值
//        for (Article article : articles) {
//            Category categoryServiceById = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(categoryServiceById.getName());
//        }
        articles.stream()
                .map(article -> {
                    article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
                    return article;
                })
                .collect(Collectors.toList());
        //只让服务器返回给浏览器需要的数据
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        //因为返回数据格式有要求，调整返回格式
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
    @Override
    public ResponseResult getArticleDetail(Long id){
        //根据id查文章
        Article article = getById(id);

        //redis键值对中存着键值对：根据ArticleViewCount键名，拿到存的值  中id键对应的值名浏览量
        Integer articleViewCount = redisCache.getCacheMapValue("ArticleViewCount", id.toString());
        article.setViewCount(articleViewCount.longValue());

        //转换成vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category categoryServiceById = categoryService.getById(categoryId);
        if(categoryId!=null){
            articleDetailVo.setCategoryName(categoryServiceById.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //incrementCacheMapValue方法在Redis以键值对的形式又存了键值对，第一个参数是Redis的键，第二个是里面存的值的键，第三个参数是每次浏览增加1
        redisCache.incrementCacheMapValue("ArticleViewCount",id.toString(),1);
        return ResponseResult.okResult();
    }


    //写博客
    @Override
    @Transactional//为了让他是一个事务的操作，失败则进行回滚
    public ResponseResult add(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);

        List<Long> tags = articleDto.getTags();
        List<ArticleTag> articleTags = tags.stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);//批量添加
        return ResponseResult.okResult();
    }
    //后台文章列表显示
    @Override
    public ResponseResult pageArticlreList(Integer pageNum, Integer pageSize, String title, String summary){
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(title),Article::getTitle,title);
        queryWrapper.like(StringUtils.hasText(summary),Article::getSummary,summary);
        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        Page<Article> page1 = page(page, queryWrapper);
        PageVo pageVo = new PageVo(page1.getRecords(), page1.getTotal());
        return ResponseResult.okResult(pageVo);
    }
    //查询文章详情接口
    @Autowired
    @Lazy
    private TagMapper tagMapper;
    @Override
    public ResponseResult getInfoById(Long id){
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId,id);
        List<Long> tagList = tagMapper.selectTagByArticleId(id);
        Article article = baseMapper.selectOne(queryWrapper);
        ArticleListParmer articleListParmer = BeanCopyUtils.copyBean(article, ArticleListParmer.class);
        articleListParmer.setTags(tagList);
        return ResponseResult.okResult(articleListParmer);
    }
    //点击后更新文章接口
    @Autowired
    ArticleTagMapper articleTagMapper;
    @Override
    public  ResponseResult updateArticle(ArticleListParmer articleListParmer){
        //在Article表中，完成对文章内容的更新
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId,articleListParmer.getId());
        Article article1 = BeanCopyUtils.copyBean(articleListParmer, Article.class);
        articleMapper.update(article1,queryWrapper);

        //删除前先获取tags，防止后面删除掉不能获取
        List<Long> tags = articleListParmer.getTags();

        //就差 List<Long> tags这一个属性没有更新
        //在ArticleTag表中，先将修改的这篇文章 的所有Tag删除掉
        LambdaQueryWrapper<ArticleTag> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ArticleTag::getArticleId,articleListParmer.getId());
        articleTagMapper.delete(queryWrapper1);
        //添加新增加的tag
        ArticleTag articleTag = new ArticleTag();
        articleTag.setArticleId(articleListParmer.getId());
        for (Long tag : tags) {
            articleTag.setTagId(tag);
            articleTagMapper.insert(articleTag);
        }
        return ResponseResult.okResult();
    }
    //通过id删除文章
    @Override
    public  ResponseResult deleteArticleById(Long id){
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getId,id);
        articleMapper.delete(articleLambdaQueryWrapper);
        return ResponseResult.okResult();
    }
}


