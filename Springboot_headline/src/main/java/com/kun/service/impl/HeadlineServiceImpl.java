package com.kun.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kun.pojo.Headline;
import com.kun.pojo.vo.PortalVo;
import com.kun.service.HeadlineService;
import com.kun.mapper.HeadlineMapper;
import com.kun.utils.JwtHelper;
import com.kun.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author WenHua
* @description 针对表【news_headline】的数据库操作Service实现
* @createDate 2024-09-07 12:43:12
*/
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
    implements HeadlineService{

    @Autowired
    private HeadlineMapper headlineMapper;

    @Autowired
    private JwtHelper jwtHelper;

    /*
    进行分页数据查询
    分页数据,拼接到result即可
    注意:
    查询需要自定义语句,自定义的mapper方法,携带分页
    返回的结果List<map>
     */
    @Override
    public Result findNewsPage(PortalVo portalVo) {
        //1.条件拼接 需要非空判断
        LambdaQueryWrapper<Headline> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(portalVo.getKeyWords()),Headline::getTitle,portalVo.getKeyWords())
                .eq(portalVo.getType()!= null,Headline::getType,portalVo.getType());
        //2.分页参数
        IPage<Headline> page = new Page<>(portalVo.getPageNum(),portalVo.getPageSize());
        //3.分页查询
        //查询的结果 "pastHours":"3"   // 发布时间已过小时数 我们查询返回一个map
        //自定义方法
        headlineMapper.selectPageMap(page,portalVo);

        Map<String,Object> data = new HashMap();
        //List<Map> records = page.getRecords();
        data.put("pageData",page.getRecords());
        data.put("pageNum",page.getCurrent());
        data.put("pageSize",page.getSize());
        data.put("totalPage",page.getPages());
        data.put("totalSize",page.getTotal());

        Map<String,Object> pageInfoMap = new HashMap();
        pageInfoMap.put("pageInfo",data);

        return Result.ok(pageInfoMap);
    }
/*
根据id查询详情
1.修改阅读量+1[version 乐观锁]
2.查询对应的数据[多表,头条和用户表,自定义方法,返回map]
 */
    @Override
    public Result showHeadlineDetail(Integer hid) {
        Map data = headlineMapper.queryDetailMap(hid);
        Map headlineMap = new HashMap();
        headlineMap.put("headline",data);

        //修改阅读量+1
        Headline headline = new Headline();
        headline.setHid((Integer) data.get("hid"));
        headline.setVersion((Integer) data.get("version"));
        //阅读量+1
        headline.setPageViews((Integer) data.get("pageViews")+1);
        headlineMapper.updateById(headline);

        return Result.ok(headlineMap);
    }

    @Override
    public Result publish(Headline headline,String token) {
        //token查询用户ID
        int userId = jwtHelper.getUserId(token).intValue();
        //数据装配
        headline.setPublisher(userId);
        headline.setPageViews(0);
        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());

        headlineMapper.insert(headline);

        return Result.ok(null);
    }
/*
hid 查询数据的最新version
修改数据的修改时间为当前时间
 */
    @Override
    public Result updateData(Headline headline) {
        Integer version = headlineMapper.selectById(headline.getHid()).getVersion();
        headline.setVersion(version);
        headline.setUpdateTime(new Date());

        headlineMapper.updateById(headline);

        return Result.ok(null);
    }
}




