package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.doman.ResponseResult;
import com.blog.doman.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-07-01 21:42:29
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    //分页查询友链列表
    ResponseResult searchLinkList(Integer pageNum, Integer pageSize, String name, String status);

    //新增友链
    ResponseResult addLinkList(Link link);

    //通过id查询友联(回显)
    ResponseResult getLinkList(Long id);

    //按下确定修改友链
    ResponseResult clickUpdateLinkList(Link link);

    //通过id删除某个友链
    ResponseResult deleteLinkList(Long id);
}

