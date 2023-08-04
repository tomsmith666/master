package com.blog.doman.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {
    private Long id;
    private String title;
    private String summary;
    private  String categoryName;
    private String content;
    private Long categoryId;
    private String thumbnail;
    private Long viewCount;
    private Date createTime;
}
