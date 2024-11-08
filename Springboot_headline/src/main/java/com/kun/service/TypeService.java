package com.kun.service;

import com.kun.pojo.Type;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kun.utils.Result;

/**
* @author WenHua
* @description 针对表【news_type】的数据库操作Service
* @createDate 2024-09-07 12:43:12
*/
public interface TypeService extends IService<Type> {
    //查询所有类型数据
    Result findAllTypes();
}
