package com.blog.doman.vo;


import com.blog.doman.entity.Role;
import com.blog.doman.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleVo {
    private List<Long> roleIds;
    private  List<Role> roles;
    private User user;
}
