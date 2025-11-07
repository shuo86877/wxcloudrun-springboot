package com.tencent.wxcloudrun.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域全局配置
 */
@Configuration
public class CorsConfig {

    @Value("${rent.jwt.header}")
    private String tokenHeader;

    /**
     * 通过过滤器的方式配置跨域
     */
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //设置访问源地址
        corsConfiguration.addAllowedOriginPattern("*");
        //设置访问源请求方式
        corsConfiguration.addAllowedMethod("*");
        //设置访问源请求头
        corsConfiguration.addAllowedHeader("*");
        //设置响应头:允许后台向前端暴露的头部信息
        corsConfiguration.addExposedHeader(tokenHeader);
        //设置后台向前端响应时允许携带凭证
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);
        //对指定接口与接口匹配设置跨域
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}