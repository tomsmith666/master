package com.blog.service.impl;

import com.blog.doman.ResponseResult;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.exception.SystemException;
import com.blog.service.UploadService;
import com.blog.utils.PathUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class OssUploadService implements UploadService {
    //更新用户头像
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //判断判断上传图片的类型
        //1.先获取文件名
        String originalFilename = img.getOriginalFilename();
        //2.根据文件后缀做判断,后缀部署png则抛出507异常
        if(!originalFilename.endsWith("png")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //封装好的PathUtils.generateFilePath，用来规范在七牛云服务器存储的文件名：日期+uuid+格式后缀(png)
        String filePath = PathUtils.generateFilePath(originalFilename);
        //拿到返回上传到七牛云服务器的图片的外链链接
        String url = OssUpload(img,filePath);
        //将拿到的ossUpload图片外链链接放到服务器返回体中，前端会将外链链接图片解析展示到用户个人主页上
        return ResponseResult.okResult(url);
    }

    private String accessKey;
    private String secretKey;
    private String bucket;

    //这个函数作用是将用户提交图片png格式上传到七牛云服务器上面，函数最后要返回上传到七牛云服务器的图片的外链链接
    private String OssUpload(MultipartFile img, String filePath){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);

        //这里负责提交给七牛云服务器的资源，设置保存路径和保存后的文件名
        String key = filePath;

        try {
//            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
//            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);


            InputStream inputStream =img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                // 返回的图片外链链接就是 http://rxdaxdail.hb-bkt.clouddn.com/   + 图片名
                return "http://rxdaxdail.hb-bkt.clouddn.com/" + filePath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "www";
    }
}
