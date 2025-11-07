package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencent.wxcloudrun.config.NineDigitMemberIdGenerator;
import com.tencent.wxcloudrun.config.WxConfig;
import com.tencent.wxcloudrun.dto.WxLoginDTO;
import com.tencent.wxcloudrun.dto.WxUser;
import com.tencent.wxcloudrun.service.WxUserService;
import com.tencent.wxcloudrun.dao.WxUserMapper;
import com.tencent.wxcloudrun.util.WxPhoneDecryptUtil;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
* @author shuo
* @description 针对表【wx_user(微信小程序用户表（带会员编号）)】的数据库操作Service实现
* @createDate 2025-10-31 08:55:09
*/
@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser>
    implements WxUserService{

    private final WxConfig wxConfig; // 存放 appid / secret
    private final RestTemplate restTemplate = new RestTemplate();

    public WxUserServiceImpl(WxConfig wxConfig) {
        this.wxConfig = wxConfig;
    }

    @Override
    public WxUser loginOrRegister(WxLoginDTO dto) throws JSONException {
        // 1️⃣ 获取 session_key 和 openid
        String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                wxConfig.getAppId(), wxConfig.getSecret(), dto.getCode()
        );
        JSONObject sessionObj = new JSONObject(restTemplate.getForObject(url, String.class));
        String openid = sessionObj.getString("openid");
        String sessionKey = sessionObj.getString("session_key");

//        // 2️⃣ 解密手机号
//        String phone = WxPhoneDecryptUtil.decrypt(dto.getEncryptedData(), sessionKey, dto.getIv());
//
        // 3️⃣ 查询数据库
        WxUser user = this.baseMapper.selectOne(
                new LambdaQueryWrapper<WxUser>().eq(WxUser::getOpenid, openid)
        );

        if (user != null) {
            // 更新最近登录信息
            user.setLastLoginTime(new Date());
            this.baseMapper.updateById(user);
            return user;
        }

        // 4️⃣ 不存在则插入新用户
        Map<String, Object> info = dto.getUserInfo();
        // 初始化：不同节点用不同 workerId, datacenterId
        NineDigitMemberIdGenerator generator = new NineDigitMemberIdGenerator(1, 1);
        WxUser newUser = new WxUser();
        newUser.setId(UUID.randomUUID().toString().replace("-", ""));
        newUser.setOpenid(openid);
        newUser.setSessionKey(sessionKey);
        newUser.setMemberId(generator.nextId());
        newUser.setNickName((String) info.get("nickName"));
        newUser.setAvatarUrl((String) info.get("avatarUrl"));
        newUser.setGender(((Number) info.get("gender")).intValue());
        newUser.setCreateTime(new Date());

        this.baseMapper.insert(newUser);
        return newUser;
    }
}




