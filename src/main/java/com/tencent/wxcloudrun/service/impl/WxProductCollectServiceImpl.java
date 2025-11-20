package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencent.wxcloudrun.dao.WxProductMapper;
import com.tencent.wxcloudrun.dto.WxProduct;
import com.tencent.wxcloudrun.dto.WxProductCollect;
import com.tencent.wxcloudrun.service.WxProductCollectService;
import com.tencent.wxcloudrun.dao.WxProductCollectMapper;
import com.tencent.wxcloudrun.service.WxProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author shuo
* @description 针对表【wx_product_collect(商品收藏表)】的数据库操作Service实现
* @createDate 2025-11-13 16:53:48
*/
@Service
public class WxProductCollectServiceImpl extends ServiceImpl<WxProductCollectMapper, WxProductCollect>
    implements WxProductCollectService{

    @Autowired
    private WxProductService wxProductService;

    @Resource
    private WxProductMapper wxProductMapper;

    @Override
    public Map<String,Object> pageVo(String userId, Integer pageNumber, Integer pageSize){
        IPage<WxProductCollect> wxProductCollectIPage = this.baseMapper.selectByUserId(userId, new Page<>(pageNumber, pageSize));
        Map<String,Object> map = new HashMap<>();
        map.put("data", wxProductCollectIPage.getRecords());
        map.put("total", wxProductCollectIPage.getTotal());
        return map;
    }

    @Override
    public boolean pageProductCount(String userId, Integer productCount, String id){
        int update = this.baseMapper.update(new WxProductCollect().setProductCount(productCount), new UpdateWrapper<WxProductCollect>().eq("user_id", userId).eq("id", id));
        return update > 0;

    }

    @Override
    public Integer calcTotalAmountInFen(String userId, List<String> idList){
        // 1. 查询 collect 列表
        List<WxProductCollect> collectList = this.baseMapper.selectList(
                new QueryWrapper<WxProductCollect>()
                        .eq("user_id", userId)
                        .in("id", idList)
        );

        if (collectList.isEmpty()) {
            return 0; // 没有收藏数据直接返回 0
        }

        BigDecimal totalAmountYuan = BigDecimal.ZERO;

        for (WxProductCollect collect : collectList) {

            // 2. 查询商品价格
            WxProduct product = wxProductService.getById(collect.getProductId());
            if (product == null) {
                continue; // 商品不存在，跳过
            }

            BigDecimal price = product.getPrice(); // 单价（元）
            Integer count = collect.getProductCount(); // 商品数量

            if (price != null && count != null) {
                // 3. 数量 × 单价
                BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(count));

                // 4. 累加金额
                totalAmountYuan = totalAmountYuan.add(itemTotal);
            }
        }

        // 5. 元 转 分（元 × 100）
        BigDecimal totalAmountFen = totalAmountYuan.multiply(BigDecimal.valueOf(100));

        // 返回 int（分）
        return totalAmountFen.intValue();
    }

    @Override
    public Map<String,Object> getCollectDataByAppIds(List<String> appIds){
        List<WxProductCollect> collects = this.baseMapper.selectList(new QueryWrapper<WxProductCollect>().in("id", appIds));
        int total = collects.size();  // collect 条数

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<Object> list = new ArrayList<>();
        // 2. 遍历 collect → 查询 product
        for (WxProductCollect collect : collects) {

            WxProduct product = wxProductMapper.selectById(collect.getProductId());
            if (product == null) continue;

            // 计算：price * count
            BigDecimal linePrice = product.getPrice()
                    .multiply(new BigDecimal(collect.getProductCount()));

            totalPrice = totalPrice.add(linePrice);


            list.add(collect);
//            item.put("product", product);
        }
        // 3. 封装返回
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("dividePrice", totalPrice.multiply(BigDecimal.valueOf(100)));
        result.put("totalPrice", "CNY " + totalPrice.toPlainString());
        result.put("list", list);

        return result;
    }

    @Override
    public Map<String, Object> getCollectData(List<String> ids, String userId) {

        List<WxProductCollect> list = baseMapper.selectByIdsAndUserId(ids, userId);

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 处理 totalPrice
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (WxProductCollect c : list) {

            WxProduct p = c.getWxProduct();

            BigDecimal price = p.getPrice();   // 商品单价
            Integer count = c.getProductCount();

            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(count));
            totalAmount = totalAmount.add(itemTotal);

            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("productId", c.getProductId());
            item.put("productCount", c.getProductCount());
            item.put("totalPrice", "CNY " + itemTotal);   // 单项金额
            item.put("wxProduct", p);

            resultList.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("count", resultList.size());
        result.put("totalAmount", totalAmount);  // 总金额
        result.put("dividePrice", totalAmount.multiply(BigDecimal.valueOf(100)));   // 单项金额
        result.put("list", resultList);

        return result;
    }
}




