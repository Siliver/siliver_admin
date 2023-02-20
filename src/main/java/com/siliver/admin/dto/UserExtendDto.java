package com.siliver.admin.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 用户扩展表
 *
 * @author siliver
 */
@Data
@TableName("t_user")
@EqualsAndHashCode(callSuper = false)
public class UserExtendDto extends Model<DictionaryDto> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "create_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(value = "create_user", updateStrategy = FieldStrategy.NEVER)
    private String createUser;

    @TableField(value = "update_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(value = "update_user")
    private String updateUser;

    @Version
    @TableField(value = "version")
    private int version;

    @TableLogic(value = "0", delval = "1")
    @TableField(value = "delete_flag")
    private int deleteFlag;

    @TableField(value = "nick_name")
    private String nickName;

    @TableField(value = "sex")
    private int sex;

    @TableField(value = "signature")
    private String signature;

    @TableField(value = "user_id", updateStrategy = FieldStrategy.NEVER)
    private int userId;
}
