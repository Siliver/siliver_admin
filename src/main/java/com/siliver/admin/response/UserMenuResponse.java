package com.siliver.admin.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "用户菜单返回实体")
public class UserMenuResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "菜单图标")
    private String menuIcon;

    @Schema(description = "菜单地址")
    private String menuUrl;

    @Schema(description = "子菜单")
    private List<UserMenuResponse> child;

    @Schema(description = "父节点编号", hidden = true)
    @JsonIgnore
    private int parentId;

    @Schema(description = "节点ID", hidden = true)
    @JsonIgnore
    private int id;

}
