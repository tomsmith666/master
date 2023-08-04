package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.constants.SystemConstants;
import com.blog.doman.ResponseResult;
import com.blog.doman.entity.Category;
import com.blog.doman.entity.Link;
import com.blog.doman.vo.LinkVo;
import com.blog.doman.vo.PageVo;
import com.blog.mapper.LinkMapper;
import com.blog.service.LinkService;
import com.blog.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-07-01 21:42:29
 */

@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
        @Autowired
        @Lazy
        LinkService linkService;
        @Override
        public ResponseResult  getAllLink(){
            LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Link::getStatus,SystemConstants.STATIC_STATUS_NORMAL);
            List<Link> list = list(queryWrapper);
            List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(list, LinkVo.class);
            return ResponseResult.okResult(linkVos);
        }

    //分页查询友链列表

    @Override
    public ResponseResult searchLinkList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        linkLambdaQueryWrapper.like(StringUtils.hasText(name),Link::getName,name);
        linkLambdaQueryWrapper.eq(StringUtils.hasText(status),Link::getStatus,status);

        Page<Link> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,linkLambdaQueryWrapper);

        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    //新增友链
    @Override
    public ResponseResult addLinkList(Link link) {
            linkService.save(link);
            return ResponseResult.okResult();
    }

    //通过id查询友联(回显)
    @Autowired
    LinkMapper linkMapper;
    @Override
    public ResponseResult getLinkList(Long id) {
        Link link = linkMapper.selectById(id);
        return  ResponseResult.okResult(link);
    }

    //按下确定修改友链
    @Override
    public ResponseResult clickUpdateLinkList(Link link) {
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        linkLambdaQueryWrapper.eq(Link::getName,link.getName());
        linkMapper.update(link,linkLambdaQueryWrapper);
        return ResponseResult.okResult();
    }

    //通过id删除某个友链
    @Override
    public ResponseResult deleteLinkList(Long id) {
        linkMapper.deleteById(id);
        return ResponseResult.okResult();
    }
}
