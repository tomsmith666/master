package com.blog.doman.vo;

import com.blog.doman.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutersVo {
   private List<Menu> menus;
}
