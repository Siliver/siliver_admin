package com.siliver.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Schema(title = "CommonChangeRequest", description = "通用维护请求参数")
public class CommonChangeRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "id数组，批量删除时使用，删除操作时如果存在此字段，则id字段无用")
    private List<Integer> ids;

    @Schema(description = "操作用户,用于实体复制使用", hidden = true)
    private String createUser;

    @Schema(description = "操作用户,用于实体复制使用", hidden = true)
    private String updateUser;
}
