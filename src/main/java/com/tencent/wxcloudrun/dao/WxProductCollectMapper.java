package com.tencent.wxcloudrun.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tencent.wxcloudrun.dto.WxProductCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author shuo
* @description 针对表【wx_product_collect(商品收藏表)】的数据库操作Mapper
* @createDate 2025-11-13 16:53:48
* @Entity com.tencent.wxcloudrun.dto.WxProductCollect
*/
public interface WxProductCollectMapper extends BaseMapper<WxProductCollect> {
    IPage<WxProductCollect> selectByUserId(@Param("userId") String userId, IPage<WxProductCollect> page);

    List<WxProductCollect> selectByIdsAndUserId(
            @Param("ids") List<String> ids,
            @Param("userId") String userId
    );
}




