package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.doman.ResponseResult;
import com.blog.doman.dto.UserDto;
import com.blog.doman.entity.Role;
import com.blog.doman.entity.User;
import com.blog.doman.entity.UserRole;
import com.blog.doman.vo.PageVo;
import com.blog.doman.vo.UserInfoVo;
import com.blog.doman.vo.UserRoleVo;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.exception.SystemException;
import com.blog.mapper.RoleMapper;
import com.blog.mapper.UserMapper;
import com.blog.mapper.UserRoleMapper;
import com.blog.service.RoleService;
import com.blog.service.UserRoleService;
import com.blog.utils.BeanCopyUtils;
import com.blog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.blog.service.UserService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-07-05 20:38:08
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult userInfo() {
        //封装好的SecurityUtils类，解析Holder中的token获得UserId
        Long userId = SecurityUtils.getUserId();

//        查询id一样的用户
        User byId = getById(userId);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(byId, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }
    @Override
    public ResponseResult uploadUser(User user) {
        updateById(user);
        return ResponseResult.okResult(user);
    }

    @Override
    public ResponseResult register(User user) {
        //为空情况
        if(!StringUtils.hasText(user.getUserName())){
            throw  new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //已存在的情况
        if(usernameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST_2);
        }
        if(passwordExist(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST_2);
        }
        if(nicknameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //多一步：加密密码再存到数据库
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        save(user);
        return ResponseResult.okResult();
    }
    private boolean usernameExist(String username){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        //若存在和表中username一样的数据，就会有用户返回，count数量就会大于0
        return count(queryWrapper)>0;
    }
    private boolean passwordExist(String password){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPassword,password);
        return count(queryWrapper)>0;
    }
    private boolean emailExist(String email){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        return count(queryWrapper)>0;
    }
    private boolean nicknameExist(String nickname){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickname);
        return count(queryWrapper)>0;
    }


    //展示用户列表
    @Override
    public ResponseResult showlist(Integer pageNum, Integer pageSize,String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        userLambdaQueryWrapper.like(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        userLambdaQueryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);

        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,userLambdaQueryWrapper);

        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    //点击确定新增用户
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Override
    public ResponseResult clickAddUser(UserDto userDto) {
        //对数据进行非空判断
        if(!StringUtils.hasText(userDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(userDto.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(userDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(userDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(userDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(userDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(emilExist(userDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //...
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodePassword);

        User user = BeanCopyUtils.copyBean(userDto, User.class);
        //存入数据库
        save(user);
        Long userId = user.getId();
        List<Long> roleIds = userDto.getRoleIds();

        List<UserRole> userRoleList = roleIds.stream()
                .map(roleId -> new UserRole(userId, roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
        return ResponseResult.okResult();
    }
    private boolean emilExist(String email) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        return count(queryWrapper)>0;
    }
    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }

    //通过id删除用户
    @Override
    public ResponseResult deleteUserById(Long id) {
        userMapper.deleteById(id);
        return ResponseResult.okResult();
    }

    //    修改用户(回显byId)
    @Autowired
    RoleService roleService;
    @Autowired
    RoleMapper roleMapper;
    @Override
    public ResponseResult updateUserById(Long id) {
        User selectById = baseMapper.selectById(id);
        UserRoleVo userRoleVo=new UserRoleVo();
        userRoleVo.setUser(selectById);

        LambdaQueryWrapper<UserRole> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        queryWrapper.select(UserRole::getRoleId);
        List<UserRole> userRoles = userRoleMapper.selectList(queryWrapper);
        List<Long> arrlist=new ArrayList<>();
        for (UserRole userRole : userRoles) {
            arrlist.add(userRole.getRoleId());
        }
        userRoleVo.setRoleIds(arrlist);
        LambdaQueryWrapper<Role> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(Role::getStatus, "0");
        List<Role> roleList = roleMapper.selectList(queryWrapper1);
        userRoleVo.setRoles(roleList);
        return ResponseResult.okResult(userRoleVo);
    }

    //更新用户信息
    @Override
    public ResponseResult clickupdateUser(UserDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        baseMapper.updateById(user);

        List<Long> roleIds = userDto.getRoleIds();
        LambdaQueryWrapper<UserRole> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,userDto.getId());
        userRoleMapper.delete(queryWrapper);
        List<UserRole> userRoleList = roleIds.stream()
                .map(roleid -> new UserRole(userDto.getId(), roleid))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
        return ResponseResult.okResult();
    }
}
