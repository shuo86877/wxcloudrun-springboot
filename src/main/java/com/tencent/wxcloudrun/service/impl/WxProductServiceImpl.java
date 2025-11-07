package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencent.wxcloudrun.dto.WxProduct;
import com.tencent.wxcloudrun.service.WxProductService;
import com.tencent.wxcloudrun.dao.WxProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
* @author shuo
* @description 针对表【wx_product(商品信息表)】的数据库操作Service实现
* @createDate 2025-11-05 16:15:04
*/
@Service
public class WxProductServiceImpl extends ServiceImpl<WxProductMapper, WxProduct>
    implements WxProductService{

    @Override
    public Map<String,Object> pageOv(Integer pageNumber, Integer pageSize, WxProduct wxProduct){
        LambdaQueryWrapper<WxProduct> lambda = new QueryWrapper<WxProduct>().lambda();
        if(StringUtils.hasLength(wxProduct.getName())){
            lambda.like(WxProduct::getName, wxProduct.getName());
        }
//        if(StringUtils.hasLength(String.valueOf(wxProduct.getPrice()))){
//            lambda.like(WxProduct::getPrice, wxProduct.getPrice());
//        }
        lambda.orderByDesc(WxProduct::getCreateBy);

        Page<WxProduct> page = this.page(new Page<>(pageNumber, pageSize), lambda);
        Map<String,Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("list", page.getRecords());
        System.out.println("map = " + map);
        return map;
    }
}




