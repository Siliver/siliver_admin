package com.siliver.admin.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典列表返回即可偶
 *
 * @author siliver
 */
@Schema(description = "字典列表对象返回实体")
@Data
public class DictionaryListResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "字典对象ID")
    private Integer id;

    @Schema(description = "字典创建时间")
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

    @Schema(description = "字典分组编码")
    private String dictionaryGroup;

    @Schema(description = "字典编号")
    private String dictionaryCode;

    @Schema(description = "字典名称")
    private String dictionaryName;

    @Schema(description = "展示类型")
    private Integer showFlag;
}
