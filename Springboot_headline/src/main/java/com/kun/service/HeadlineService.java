package com.kun.service;

import com.kun.pojo.Headline;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kun.pojo.vo.PortalVo;
import com.kun.utils.Result;

/**
* @author WenHua
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2024-09-07 12:43:12
*/
public interface HeadlineService extends IService<Headline> {

    //首页数据查询
    Result findNewsPage(PortalVo portalVo);
    //根据id查询详情
    Result showHeadlineDetail(Integer hid);
    //发布头条方法
    Result publish(Headline headline,String token);
    //修改头条数据
    Result updateData(Headline headline);
}
