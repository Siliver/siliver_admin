package com.siliver.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色修改请求参数
 *
 * @author siliver
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "RoleChangeRequest", description = "角色维护请求参数")
public class RoleChangeRequest extends CommonChangeRequest {

    @Schema(description = "角色编号")
    private String roleCode;

    @Schema(description = "角色名称")
    private String roleName;
}
