package com.kun.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kun.pojo.Headline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kun.pojo.vo.PortalVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author WenHua
* @description 针对表【news_headline】的数据库操作Mapper
* @createDate 2024-09-07 12:43:12
* @Entity com.kun.domain.Headline
*/
public interface HeadlineMapper extends BaseMapper<Headline> {
    IPage<Map> selectPageMap (IPage<Headline> page, @Param("portalVo") PortalVo portalVo);

    Map queryDetailMap(Integer hid);
}




