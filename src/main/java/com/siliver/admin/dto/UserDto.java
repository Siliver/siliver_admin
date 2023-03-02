package com.siliver.admin.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 用户基础表
 *
 * @author siliver
 */
@Data
@TableName("t_user")
@EqualsAndHashCode(callSuper = false)
public class UserDto extends Model<UserDto> {

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

    @TableField(value = "username", updateStrategy = FieldStrategy.NEVER)
    private String username;

    @TableField("password")
    private String password;

    @TableField("credentialsNonExpired")
    private boolean credentialsNonExpired;

    @TableField("accountNonLocked")
    private boolean accountNonLocked;

    @TableField("accountNonExpired")
    private boolean accountNonExpired;

    @TableField(value = "last_login_time", insertStrategy = FieldStrategy.NEVER)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;

    @TableField(value = "lock_count", insertStrategy = FieldStrategy.NEVER)
    private int lockCount;

    @TableField(value = "unlock_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime unlockTime;

    @TableField(exist = false)
    private Long expiration;

}
