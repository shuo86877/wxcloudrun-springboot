package com.tencent.wxcloudrun.util;

import com.alibaba.fastjson2.JSONObject;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class WxPhoneDecryptUtil {
    /**
     * 解密手机号
     * @param encryptedData 微信返回的加密数据
     * @param sessionKey 登录会话密钥（通过 jscode2session 获取）
     * @param iv 加密算法初始向量
     * @return 明文手机号
     */
    public static String decrypt(String encryptedData, String sessionKey, String iv) {
        try {
            byte[] dataByte = Base64.getDecoder().decode(encryptedData);
            byte[] keyByte = Base64.getDecoder().decode(sessionKey);
            byte[] ivByte = Base64.getDecoder().decode(iv);

            // 处理 key 不足 16 位问题
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, spec, ivSpec);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (resultByte != null && resultByte.length > 0) {
                String result = new String(resultByte, StandardCharsets.UTF_8);
                JSONObject obj = JSONObject.parseObject(result);
                return obj.getString("phoneNumber");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
