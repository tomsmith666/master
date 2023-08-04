package com.blog.doman.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDto {
    private  Long id;

    private String roleId;
    private String status;

    //角色名称
    private String roleName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;

    //备注
    private String remark;
    private List<Long> menuIds;

}
