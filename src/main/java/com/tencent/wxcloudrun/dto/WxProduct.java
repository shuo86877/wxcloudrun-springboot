package com.tencent.wxcloudrun.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 商品信息表
 * @TableName wx_product
 */
@TableName(value ="wx_product")
@Data
public class WxProduct {
    /**
     * 商品ID（主键，非自增）
     */
    @TableId
    private String id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品原价
     */
    private BigDecimal originalPrice;

    /**
     * 
     */
    private Object recommend;

    /**
     * 商品详情主题
     */
    private String detailTheme;

    /**
     * 商品类型
     */
    private String type;

    /**
     * 商品图片信息与顺序，如：[{"order":1,"url":"xxxx"}]
     */
    private Object images;

    /**
     * 退订规则
     */
    private String unsubscribeRule;

    /**
     * 有效日期，如：2025-11-10
     */
    private Date validDate;

    /**
     * 使用规则
     */
    private String useRule;

    /**
     * 温馨提示
     */
    private String tips;

    /**
     * 其他推荐，如：[{"id":"xxx","name":"xxx"}]
     */
    private Object otherRecommend;

    /**
     * 服务说明
     */
    private String serviceDesc;

    /**
     * 商品创建人
     */
    private String createBy;

    /**
     * 商品修改人
     */
    private String updateBy;

    /**
     * 商品创建时间
     */
    private Date createTime;

    /**
     * 商品修改时间
     */
    private Date updateTime;

    /**
     * 商品售卖地址
     */
    private String sellAddress;

    /**
     * 酒店名称
     */
    private String hotelName;

    /**
     * 酒店商品类目类型
     */
    private String hotelCategoryType;

    /**
     * 轮播图集合
     */
    private Object sliderImage;

    /**
     * 不可用日期
     */
    private String invalidDate;

    /**
     * 可用日期
     */
    private String availableDate;
}