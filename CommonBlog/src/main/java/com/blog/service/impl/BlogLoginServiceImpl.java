package com.blog.service.impl;

import com.blog.doman.ResponseResult;
import com.blog.doman.entity.LoginUser;
import com.blog.doman.entity.User;
import com.blog.doman.vo.BlogUserLoginVo;
import com.blog.doman.vo.UserInfoVo;
import com.blog.service.BlogLoginService;
import com.blog.utils.BeanCopyUtils;
import com.blog.utils.JwtUtil;
import com.blog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //new一个UsernamePasswordAuthenticationToken，将输入的用户名和密码封装成一个authenticationToken对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        //authenticationManager帮我们进行认证校验操作，会调用其authenticate方法，
        // (经过几个流程)最终会调用UserDetailsServiceImpl实现类的loadUserByUsername方法进行校验，
        // 给该方法的就是用户提交的用户名，在里面根据用户名查询对应一名用户信息，将其 封装 然后return
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //因为getPrincipal()后获取的结果是LoginUser类型的，所以要进行强转(成LoginUser)
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        //用getUser()拿到user对象，getId()获取userid
        String userId = loginUser.getUser().getId().toString();
        //用jwt将userId进行加密，生成token(因为以后浏览器携带token再次发请求时浏览器解析token，根据id就能获得对应用户信息，所以只加密id)
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息和id存入redis
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);

        //把token和userinfo封装 返回
        //把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(vo);
    }

    @Override
    public  ResponseResult logout(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }


}