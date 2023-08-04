package com.blog.doman.dto;

import com.blog.doman.vo.MenuTreeVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuDto {
    List<MenuTreeVo> menus;
    List<Long>checkedKeys;
}
