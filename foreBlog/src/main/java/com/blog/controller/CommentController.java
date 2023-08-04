package com.blog.controller;

import com.blog.doman.ResponseResult;
import com.blog.doman.entity.Comment;
import com.blog.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论" ,description = "评论相关接口")
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
//        （0代表文章评论，1代表友链评论）
        return commentService.commentList("0",articleId,pageNum,pageSize);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    @ApiOperation("友链评论列表")
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
    //        （0代表文章评论，1代表友链评论）
        return commentService.commentList("1", null,pageNum,pageSize);
    }
}
