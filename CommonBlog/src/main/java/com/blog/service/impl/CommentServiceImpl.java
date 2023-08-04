package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.constants.SystemConstants;
import com.blog.doman.ResponseResult;
import com.blog.doman.entity.Comment;
import com.blog.doman.vo.CommentVo;
import com.blog.doman.vo.PageVo;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.exception.SystemException;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import com.blog.service.UserService;
import com.blog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-07-05 19:32:34
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType,Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断
        queryWrapper.eq(Comment::getArticleId,articleId);
        //根评论 rootId为-1
        queryWrapper.eq(Comment::getRootId,-1);

        //分页查询
        Page<Comment> page = new Page(pageNum,pageSize);
        page(page,queryWrapper);
        //所有根评论
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }

        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    //添加子查询的 getChildren方法
    private List<CommentVo> getChildren(Long id) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //传来的根评论id和Comment表中getRootId相一致的，筛选出来都是子评论
        queryWrapper.eq(Comment::getRootId,id);
//        //子评论按时间排序
        queryWrapper.orderByAsc(Comment::getCreateTime);
        //变成List
        List<Comment> comments = list(queryWrapper);
        //为CommentVo中新增加的toCommentUserName和username两属性赋值
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }
    //这个方法作用就是为CommentVo中新增加的toCommentUserName和username两属性赋值，封装方便多次使用
    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //因为CommentVo中新增加了toCommentUserName和username两个属性，
        //这两个属性在comment表里面是没有的，所以需要进行获取，用UserServiceImpl来获取
        for (CommentVo commentVo : commentVos) {
            //commentVo.getCreateBy()作用找到发表对应根评论的人的id
            //userService.getById是在User表里面用id找人,再找他的nickname网站假名(对应CommentVo中新增username属性)
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //toCommentUserName属性同上，但根评论没有对别人的回复，所以没toCommentUserName，
            //多一步排除(getToCommentId()=-1为根评论)
            if(commentVo.getToCommentUserId()!=-1){
                String toCommentUserName =userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }
    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }
}