package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.WxProductCollect;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author shuo
* @description 针对表【wx_product_collect(商品收藏表)】的数据库操作Service
* @createDate 2025-11-13 16:53:48
*/
public interface WxProductCollectService extends IService<WxProductCollect> {

    Map<String,Object> pageVo(String userId, Integer pageNumber, Integer pageSize);

    boolean pageProductCount(String userId, Integer productCount, String id);

    Integer calcTotalAmountInFen(String userId, List<String> idList);

    Map<String,Object> getCollectDataByAppIds(List<String> appIds);

    Map<String, Object> getCollectData(List<String> ids, String userId);
}
