package com.siliver.admin.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 角色菜单关联表
 *
 * @author siliver
 */
@Data
@TableName("t_role_menu")
@EqualsAndHashCode(callSuper = false)
public class RoleMenuDto extends Model<RoleMenuDto> {

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

    @TableField(value = "role_id", updateStrategy = FieldStrategy.NEVER)
    private Integer roleId;

    @TableField(value = "menu_id", updateStrategy = FieldStrategy.NEVER)
    private Integer menuId;
}
