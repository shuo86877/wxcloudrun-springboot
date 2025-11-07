package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.WxLoginDTO;
import com.tencent.wxcloudrun.dto.WxUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.json.JSONException;

/**
* @author shuo
* @description 针对表【wx_user(微信小程序用户表（带会员编号）)】的数据库操作Service
* @createDate 2025-10-31 08:55:09
*/
public interface WxUserService extends IService<WxUser> {

    WxUser loginOrRegister(WxLoginDTO dto) throws JSONException;
}
