package com.tencent.wxcloudrun.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.WxProductCollect;
import com.tencent.wxcloudrun.service.WxProductCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/product/collect")
public class WxProductCollectController {
    @Autowired
    private WxProductCollectService wxProductCollectService;


    /***
     * 查询出userId关联的表与表关系
     * @param userId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping("/getPageVo")
    public ApiResponse getPageVo(String userId, Integer pageNumber, Integer pageSize) {
        Map<String, Object> stringObjectMap = wxProductCollectService.pageVo(userId, pageNumber, pageSize);
        return ApiResponse.ok(stringObjectMap);
    }

    @GetMapping("/getPageProduct")
    public ApiResponse getProductCount(String userId, Integer productCount, String id){
        boolean b = wxProductCollectService.pageProductCount(userId, productCount, id);
        return ApiResponse.ok(b);
    }

    @PostMapping("/calcTotal")
    public ApiResponse calcTotal(@RequestBody Map<String, Object> params) {
        String userId = (String) params.get("userId");
        List<String> idList = (List<String>) params.get("idList");
        if(userId == null || idList == null || idList.size() == 0){
            return ApiResponse.ok(0);
        }
        Integer totalFen = wxProductCollectService.calcTotalAmountInFen(userId, idList);
        return ApiResponse.ok(totalFen);
    }

    @PostMapping("/getAddByCollect")
    public ApiResponse addProductCollect(@RequestBody WxProductCollect wxProductCollect) {
        List<WxProductCollect> list = wxProductCollectService.list(new QueryWrapper<WxProductCollect>().lambda().eq(WxProductCollect::getUserId, wxProductCollect.getUserId()).eq(WxProductCollect::getProductId, wxProductCollect.getProductId()));
        if (list.size() > 0) {
            Integer productCount = list.get(0).getProductCount();
            productCount = productCount + 1;
            list.get(0).setProductCount(productCount);
            list.get(0).setUpdateTime(new Date());
            wxProductCollectService.updateById(list.get(0));
            return ApiResponse.ok();
        } else {
            wxProductCollect.setId(UUID.randomUUID().toString().replace("-", ""));
            wxProductCollect.setCreateTime(new Date());
            wxProductCollect.setUpdateTime(new Date());
            wxProductCollectService.save(wxProductCollect);
            return ApiResponse.ok();
        }
    }


    @PostMapping("/getByAppIds")
    public ApiResponse getByAppIds(@RequestBody Map<String, Object> params) {
        List<String> appIds = (List<String>) params.get("appId");
        System.out.println("appIds = " + appIds);
        return ApiResponse.ok(wxProductCollectService.getCollectDataByAppIds(appIds));
    }


    /**
     * 根据 collectId 列表查询收藏数据 + 对应的商品信息
     */
    @PostMapping("/listByIds")
    public ApiResponse listByIds(@RequestBody Map<String, Object> params) {
        List<String> ids = (List<String>) params.get("ids");
        String userId = (String) params.get("userId");
        return ApiResponse.ok(wxProductCollectService.getCollectData(ids,userId));
    }



}
