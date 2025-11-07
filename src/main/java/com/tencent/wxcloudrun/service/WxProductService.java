package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.WxProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author shuo
* @description 针对表【wx_product(商品信息表)】的数据库操作Service
* @createDate 2025-11-05 16:15:04
*/
public interface WxProductService extends IService<WxProduct> {

    Map<String,Object> pageOv(Integer pageNumber, Integer pageSize, WxProduct wxProduct);
}
