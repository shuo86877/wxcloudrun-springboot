package com.tencent.wxcloudrun.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 微信小程序用户表（带会员编号）
 * @TableName wx_user
 */
@TableName(value ="wx_user")
@Data
public class WxUser {
    /**
     * 主键ID（由业务层生成，如雪花算法）
     */
    @TableId
    private String id;

    /**
     * 会员编号（9位数字，雪花算法生成，唯一）
     */
    private Long memberId;

    /**
     * 微信小程序唯一标识（openid）
     */
    private String openid;

    /**
     * 微信开放平台唯一标识（unionid）
     */
    private String unionid;

    /**
     * 微信登录会话密钥（session_key）
     */
    private String sessionKey;

    /**
     * 微信昵称
     */
    private String nickName;

    /**
     * 用户头像URL
     */
    private String avatarUrl;

    /**
     * 性别：0未知 1男 2女
     */
    private Integer gender;

    /**
     * 语言（如 zh_CN）
     */
    private String language;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 手机号（含区号）
     */
    private String phoneNumber;

    /**
     * 手机号（不含区号）
     */
    private String purePhoneNumber;

    /**
     * 国家区号（如 +86）
     */
    private String countryCode;

    /**
     * 用户状态：1正常 0禁用
     */
    private Integer status;

    /**
     * 会员等级
     */
    private Integer vipLevel;

    /**
     * 用户积分
     */
    private Integer points;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 最近登录IP
     */
    private String loginIp;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除标识：0未删 1已删
     */
    private Integer deleted;
}