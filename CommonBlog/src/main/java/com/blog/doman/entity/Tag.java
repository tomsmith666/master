package com.blog.doman.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.ToString;

/**
 * 标签(Tag)表实体类
 *
 * @author makejava
 * @since 2023-07-11 18:37:19
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sg_tag")
public class Tag  {
    @TableId
    private Long id;

    //标签名
    private String name;

    //添加这行语句后，插入的数据在创建时添加创建的时间
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    //添加这行语句后，插入的数据在创建时添加创建的时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //添加这行语句后，插入的数据在更新时会自动变更时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    //添加这行语句后，插入的数据在更新时会自动变更时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
    //备注
    private String remark;



}

