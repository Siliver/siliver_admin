package com.siliver.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典修改参数
 *
 * @author siliver
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "DictionaryChangeRequest", description = "字典维护请求参数")
public class DictionaryChangeRequest extends CommonChangeRequest {

    @NotBlank(message = "字典分组不能为空")
    @Schema(description = "字典分组")
    private String dictionaryGroup;

    @NotBlank(message = "字典代码不能为空")
    @Schema(description = "字典代码")
    private String dictionaryCode;

    @NotBlank(message = "字典名称不能为空")
    @Schema(description = "字典名称")
    private String dictionaryName;

    @Min(value = 0, message = "展示类型错误")
    @Max(value = 2, message = "展示类型错误")
    @NotBlank(message = "展示类型不能为空")
    @Schema(description = "展示类型")
    private Integer showFlag;

}
