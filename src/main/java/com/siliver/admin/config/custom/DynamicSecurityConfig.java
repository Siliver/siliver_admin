package com.siliver.admin.config.custom;

import lombok.Data;

import java.util.List;

/**
 * 动态权限配置
 */
@Data
public class DynamicSecurityConfig {

    private String name;

    private List<String> url;
}
