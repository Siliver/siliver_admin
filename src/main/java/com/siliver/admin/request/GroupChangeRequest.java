package com.siliver.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "GroupChangeRequest", description = "用户组维护请求参数")
public class GroupChangeRequest extends CommonChangeRequest {

    @Schema(description = "用户组编号")
    private String groupCode;

    @Schema(description = "用户组名称")
    private String groupName;
}
