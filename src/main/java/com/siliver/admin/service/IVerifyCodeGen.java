package com.siliver.admin.service;

import com.siliver.admin.common.Result;
import com.siliver.admin.model.VerifyCodeModel;

/**
 * 验证码相关接口
 *
 * @author siliver
 */
public interface IVerifyCodeGen {

    /**
     * 生成验证码对象
     *
     * @param width  图片宽度
     * @param height 图片高度
     * @return 生成对象
     */
    VerifyCodeModel generate(int width, int height);

    /**
     * 验证码验证
     *
     * @param requestOnce 验证码标识
     * @param code        验证码
     * @return 验证结果
     */
    Result<Void> verifyCode(String requestOnce, String code);
}
