package com.siliver.admin.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单管理返回列表实体对象
 *
 * @author siliver
 */
@Data
@Schema(description = "菜单管理返回列表实体对象")
public class MenuManageListResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "角色ID")
    private int id;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private String createUser;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    private String updateUser;

    @Schema(description = "删除标识")
    private int deleteFlag;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "父节点ID")
    private Integer parentId;

    @Schema(description = "菜单icon地址")
    private String menuIcon;

    @Schema(description = "菜单关联地址")
    private String menuUrl;
}
