package com.blog.constants;

public class SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是已发布
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    //管理文件是否正常发布状态
    public static final String STATUS_NORMAL = "0";
    //负责管理友联是否已经审核，0代表已审核
    public static final String STATIC_STATUS_NORMAL = "0";
    //管理commentList方法第一个参数，用来判断区分是文章评论还是友链评论（0代表文章评论，1代表友链评论）
    public static final String ARTICLE_COMMENT = "0";
    
}