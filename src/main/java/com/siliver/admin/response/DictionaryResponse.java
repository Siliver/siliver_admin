package com.siliver.admin.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户端字典返回
 *
 * @author siliver
 */
@Schema(description = "字典对象返回实体")
@Data
public class DictionaryResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "字典名称")
    public String name;

    @Schema(description = "字典值")
    public String value;
}
