package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.util.Map;

@Data
public class WxLoginDTO {
    private String code; // wx.login 返回的 code
    private String encryptedData; // 手机号加密数据
    private String iv; // 手机号加密向量
    private Map<String, Object> userInfo; // 包含昵称、头像等
}
