package com.tencent.wxcloudrun.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.WxProduct;
import com.tencent.wxcloudrun.service.WxProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/WxProduct")
public class WxProductController {
    @Autowired
    private WxProductService wxProductService;


    /***
     * 获取全部商品信息
     */
    @GetMapping("/list/{type}")
    public ApiResponse getList(@PathVariable("type") String type){
        LambdaQueryWrapper<WxProduct> eq = new QueryWrapper<WxProduct>().lambda().eq(WxProduct::getType, type);
        List<WxProduct> list = wxProductService.list(eq);
        return ApiResponse.ok(list);
    }

    @GetMapping("/getById/{id}")
    public ApiResponse getById(@PathVariable("id") String id){
        return ApiResponse.ok(wxProductService.getById(id));
    }

    @PostMapping("/getByAdd")
    public ApiResponse add(@RequestBody WxProduct wxProduct){
        wxProduct.setId(UUID.randomUUID().toString().replace("-", ""));
        wxProduct.setCreateTime(new Date());
        wxProduct.setUpdateTime(new Date());
        return ApiResponse.ok(wxProductService.save(wxProduct));
    }
    @PostMapping("/pageVo")
    public ApiResponse pageVo(Integer pageNumber, Integer pageSize,@RequestBody WxProduct wxProduct){
        return ApiResponse.ok(wxProductService.pageOv(pageNumber,pageSize,wxProduct));
    }

    @PutMapping("/edit")
    public ApiResponse edit(@RequestBody WxProduct wxProduct){
        wxProduct.setUpdateTime(new Date());
        return ApiResponse.ok(wxProductService.updateById(wxProduct));
    }
}
