package com.kun.service;

import com.kun.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kun.utils.Result;

/**
* @author WenHua
* @description 针对表【news_user】的数据库操作Service
* @createDate 2024-09-07 12:43:12
*/
public interface UserService extends IService<User> {

    //登陆业务
    Result login(User user);

    //根据token获取用户数据
    Result getUserInfo(String token);

    Result checkUserName(String username);

    Result regist(User user);
}
