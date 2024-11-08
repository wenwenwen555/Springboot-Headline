package com.kun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kun.pojo.Type;
import com.kun.service.TypeService;
import com.kun.mapper.TypeMapper;
import com.kun.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author WenHua
* @description 针对表【news_type】的数据库操作Service实现
* @createDate 2024-09-07 12:43:12
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{

    @Autowired
    private TypeMapper typeMapper;
    @Override
    public Result findAllTypes() {
        List<Type> types = typeMapper.selectList(null);

        return Result.ok(types);
    }
}




