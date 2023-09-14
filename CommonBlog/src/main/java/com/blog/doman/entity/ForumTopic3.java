package com.blog.doman.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (ForumTopic3)表实体类
 *
 * @author makejava
 * @since 2023-09-07 19:40:59
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("forum_topic3")
//从mongo中取数据存到mysql里面，这个是mysql的实体类
public class ForumTopic3  {
    @TableId
    private Integer id;
    private String encryptid;

    
    private String wechatid;
    
    private String email;
    
    private String name;
    
    private String phone;
    
    private String location;

    private String url;

}

