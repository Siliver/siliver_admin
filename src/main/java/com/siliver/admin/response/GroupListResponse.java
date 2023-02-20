package com.siliver.admin.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "用户组列表返回实体")
public class GroupListResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "用户组编号")
    private String groupCode;

    @Schema(description = "用户组名称")
    private String groupName;

    @Schema(description = "子用户组")
    private List<GroupListResponse> children;

}