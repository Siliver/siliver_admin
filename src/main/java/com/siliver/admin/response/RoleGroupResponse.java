package com.siliver.admin.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色分组列表返回实体
 *
 * @author siliver
 */
@Data
@Schema(description = "角色分组列表返回实体")
public class RoleGroupResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "角色ID")
    private int groupId;

    @Schema(description = "角色编号")
    private String groupCode;

    @Schema(description = "角色名称")
    private String groupName;

    @Schema(description = "关联ID")
    private int relationId;
}
