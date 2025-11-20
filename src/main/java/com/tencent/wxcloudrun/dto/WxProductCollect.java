package com.tencent.wxcloudrun.dto;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品收藏表
 * @TableName wx_product_collect
 */
@TableName(value ="wx_product_collect")
@Data
@Accessors(chain = true)
public class WxProductCollect {
    /**
     * 主键ID（非自增）
     */
    @TableId
    private String id;

    /**
     * 商品ID，关联wx_product.id
     */
    private String productId;

    /**
     * 用户账号ID
     */
    private String userId;

    /**
     * 收藏时间
     */
    private Date createTime;

    /**
     * 商品数量
     */
    private Integer productCount;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除（0 未删除，1 已删除）
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private WxProduct wxProduct;
}