package com.blog.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.blog.doman.ResponseResult;
import com.blog.doman.dto.CategoryDto;
import com.blog.doman.entity.Category;
import com.blog.doman.vo.ExcelCategoryVo;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.service.CategoryService;
import com.blog.utils.BeanCopyUtils;
import com.blog.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

//    写博客 查询所有分类接口
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryDto> categoryDtos=categoryService.listAllCategory();
        return ResponseResult.okResult(categoryDtos);
    }

//    点击导出将所有分类导到excel表格中
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xls",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    //分页查询分类列表
    @GetMapping("/list")
    public ResponseResult export(Integer pageNum,Integer pageSize,String name,String status){
        return categoryService.getcategoryList(pageNum,pageSize,name,status);
    }

    //新增分类
    @PostMapping
    public ResponseResult addcategory(@RequestBody Category category){
        return categoryService.addcategory(category);
    }

    //通过id修改分类(回显)
    @GetMapping("{id}")
    public ResponseResult updatecategoryById(@PathVariable Long id){
        return categoryService.updatecategoryById(id);
    }

    //按下确定更新分类
    @PutMapping
    public ResponseResult clickUpdateCategory(@RequestBody Category category){
        return categoryService.clickUpdateCategory(category);
    }

    //通过id删除某个分类
    @DeleteMapping("{id}")
    public ResponseResult deleteCategoryById(@PathVariable Long id){
        return categoryService.deleteCategoryById(id);
    }


}
