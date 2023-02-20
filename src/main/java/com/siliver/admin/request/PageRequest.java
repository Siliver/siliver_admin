package com.siliver.admin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(title = "PageRequest", description = "分页请求参数")
public class PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @NotBlank(message = "请输入页码")
    @Schema(description = "页码")
    @JsonProperty(value = "current")
    private Long page;

    @NotBlank(message = "请输入页大小")
    @Schema(description = "页大小")
    @JsonProperty(value = "size")
    private Long pageSize;
}
