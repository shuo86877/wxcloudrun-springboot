package com.tencent.wxcloudrun.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置类
 * 负责读取 application.yml 中 wx.* 配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WxConfig {
    /** 小程序 AppID */
    private String appId;

    /** 小程序 AppSecret */
    private String secret;
}
