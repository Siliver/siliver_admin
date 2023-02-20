package com.siliver.admin.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 用户
 *
 * @author siliver
 */
@Data
@TableName("t_user_group")
@EqualsAndHashCode(callSuper = false)
public class UserGroupDto extends Model<UserGroupDto> {

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
    @TableField("version")
    private int version;

    @TableLogic(value = "0", delval = "1")
    @TableField("delete_flag")
    private int deleteFlag;

    @TableField(value = "user_id", updateStrategy = FieldStrategy.NEVER)
    private Integer userId;

    @TableField("group_id")
    private Integer groupId;

}
