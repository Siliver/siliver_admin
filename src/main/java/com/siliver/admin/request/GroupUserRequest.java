package com.siliver.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户组用户编辑参数
 *
 * @author siliver
 */
@Data
@Schema(title = "GroupUserRequest", description = "用户组用户维护请求参数")
public class GroupUserRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "用户组ID")
    private int groupId;

    @Schema(description = "用户ID集合")
    private List<Integer> userIds;
}
