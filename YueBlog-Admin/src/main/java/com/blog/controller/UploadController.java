//package com.blog.controller;
//
//import com.blog.utils.QiniuUtils;
//import com.blog.utils.ResponseResult;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.UUID;
//
//将这个类注释掉，因为七牛云服务器存储更换的头像，但免费的七牛云每15天就要更换服务器什么的，比较麻烦，就将这个功能注释掉
//@RestController
//@RequestMapping("upload")
//public class UploadController {
//    @Autowired
//    private QiniuUtils qiniuUtils;
//    @PostMapping
//    public ResponseResult upload(@RequestParam("img") MultipartFile file){
//        if(file.isEmpty()){
//            return ResponseResult.errorResult(508,"上传失败");
//        }
//        //原始文件名
//        String originalFilename = file.getOriginalFilename();
//        //唯一文件名
//        String fileName= UUID.randomUUID().toString()+"."+ StringUtils.substringAfterLast(originalFilename,".");
//        boolean upload = qiniuUtils.upload(file, fileName);
//        if(upload){
//            return ResponseResult.okResult(QiniuUtils.url+fileName);
//        }
//        return ResponseResult.errorResult(508,"上传失败");
//    }
//}