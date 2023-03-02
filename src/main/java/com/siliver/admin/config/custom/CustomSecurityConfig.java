package com.siliver.admin.config.custom;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 自定义白/黑名单
 *
 * @author siliver
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "custom.security")
@EnableConfigurationProperties(value = CustomSecurityConfig.class)
public class CustomSecurityConfig {

    /**
     * 白名单
     */
    private List<String> white;

    /**
     * 跳过
     */
    private List<String> skip;

    /**
     * 角色关联名单
     */
    private List<DynamicSecurityConfig> dynamic;
}
