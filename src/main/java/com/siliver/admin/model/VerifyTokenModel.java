package com.siliver.admin.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * token验证结果类
 *
 * @author siliver
 */
@Data
public class VerifyTokenModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    private String username;

    private int userId;

    private List<String> roles;
}
