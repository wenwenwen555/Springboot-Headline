package com.kun.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kun.pojo.User;
import com.kun.service.UserService;
import com.kun.mapper.UserMapper;
import com.kun.utils.JwtHelper;
import com.kun.utils.MD5Util;
import com.kun.utils.Result;
import com.kun.utils.ResultCodeEnum;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author WenHua
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2024-09-07 12:43:12
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    /*
根据账号查询用户对象
如果账号为空,查询失败,账号错误
对比密码,返回503错误代码
根据用户id生产一个token
 */
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtHelper jwtHelper;
    @Override
    public Result login(User user) {

        //根据账号查询数据库
        LambdaQueryWrapper<User> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.eq(User::getUsername,user.getUsername());
        User loginUser = userMapper.selectOne(LambdaQueryWrapper);

        if (loginUser == null) {
            return  Result.build(null, ResultCodeEnum.USERNAME_ERROR);
            //501异常账号错误
        }
        //对比密码
        if (!StringUtils.isEmpty(user.getUserPwd()) &&
                MD5Util.encrypt(user.getUserPwd()).equals(loginUser.getUserPwd())) {
            //登出成功
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid()));
            //根据用户ID生成token 封装到result返回
            Map data = new HashMap();
            data.put("token",token);

            return Result.ok(data);
        }
        //密码错误
        return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
        //503密码错误
    }

    /*
    校验token是否在有效期
    根据token解析userID
    根据id查询用户数据
    去除密码封装result结果返回
     */
    @Override
    public Result getUserInfo(String token) {

        boolean expiration = jwtHelper.isExpiration(token);

        if (expiration) {
            //失败当作未登录看待
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }

        int userId = jwtHelper.getUserId(token).intValue();

        User user = userMapper.selectById(userId);
        user.setUserPwd("");

        Map data = new HashMap();
        data.put("loginUser", user);

        return Result.ok(data);
    }

    /*检查账号是否可用
    根据账号进行count查询
    count == 0 可用
    count > 0 不可用
     */
    @Override
    public Result checkUserName(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        Long count = userMapper.selectCount(queryWrapper);

        if (count == 0) {
            return Result.ok(null);
        }
        return Result.build(null,ResultCodeEnum.USERNAME_USED);
    }
    /*
    注册业务
    检查账号是否被注册
    密码加密处理
    账号数据保存
    返回结果
     */
    @Override
    public Result regist(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
        Long count = userMapper.selectCount(queryWrapper);

        if (count > 0) {
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }
        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        userMapper.insert(user);
        return Result.ok(null);
    }
}




