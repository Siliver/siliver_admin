package com.siliver.admin.config.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wechat")
@EnableConfigurationProperties(value = WeChatConfig.class)
public class WeChatConfig {

    private String appId;

    private String appSecret;
}
