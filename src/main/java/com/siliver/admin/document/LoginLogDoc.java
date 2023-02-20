package com.siliver.admin.document;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 登录日志
 */
@Document("login_list")
@Data
public class LoginLogDoc implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @MongoId
    private String id;

    @Field(value = "username", order = 1)
    private String username;

    @Field(value = "login_time", order = 2)
    private LocalDateTime loginTime;

    @Field(value = "roles", targetType = FieldType.ARRAY, order = 3)
    private List<Integer> roles;
}
