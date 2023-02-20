package com.siliver.admin.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户组用户列表返回实体
 *
 * @author siliver
 */
@Data
@Schema(description = "用户组用户列表返回实体")
public class GroupUserListResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "用户ID")
    private int userId;

    @Schema(description = "用户编号")
    private String userCode;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "关联ID")
    private int relationId;
}

