package com.siliver.admin.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单角色列表返回实体
 *
 * @author siliver
 */
@Data
@Schema(description = "菜单角色列表返回实体")
public class MenuRoleListResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "角色ID")
    private int roleId;

    @Schema(description = "角色编号")
    private String roleCode;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "关联ID")
    private int relationId;
}
