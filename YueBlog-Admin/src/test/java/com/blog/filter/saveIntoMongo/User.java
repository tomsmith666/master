package com.blog.filter.saveIntoMongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//这个表是mongo的表
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * 将url进行md5加密作为独一无二的id
     */
    private String encryptid;

    /**
     * 每个列表的url
     */
    private String url;

    /**
     * 列表数字名(正则拿到的一串数字)
     */
    private String name;

    /**
     * 列表页html
     */
    @JsonIgnore
    private String pageHtml;

    /**
     * 是否测试抓取(测试类时候设置为true)
     */
    private boolean forTest;

    /**
     * 电话号码
     */
    private String telephone;
    private String weixin;
    private String location;
    private String email;
}