package com.siliver.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单维护请求参数
 *
 * @author siliver
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "MenuChangeRequest", description = "菜单维护请求参数")
public class MenuChangeRequest extends CommonChangeRequest {

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "菜单图标")
    private String menuIcon;

    @Schema(description = "菜单地址")
    private String menuUrl;
}
