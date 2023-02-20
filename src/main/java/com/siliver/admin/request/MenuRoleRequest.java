package com.siliver.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单角色维护请求参数
 *
 * @author siliver
 */
@Data
@Schema(title = "MenuRoleRequest", description = "菜单角色维护请求参数")
public class MenuRoleRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "菜单ID")
    private int menuId;

    @Schema(description = "角色ID集合")
    private List<Integer> roleIds;
}
