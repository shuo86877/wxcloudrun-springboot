package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.WxLoginDTO;
import com.tencent.wxcloudrun.dto.WxUser;
import com.tencent.wxcloudrun.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/WxUser")
public class WxUserController {
    @Autowired
    private WxUserService wxUserService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody WxLoginDTO dto) throws JSONException {
        WxUser user = wxUserService.loginOrRegister(dto);
        return ApiResponse.ok(user);
    }
}
