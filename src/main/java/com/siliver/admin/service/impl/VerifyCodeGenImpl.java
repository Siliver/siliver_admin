package com.siliver.admin.service.impl;

import com.siliver.admin.common.Result;
import com.siliver.admin.model.VerifyCodeModel;
import com.siliver.admin.service.IVerifyCodeGen;
import com.siliver.admin.util.CustomRandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyCodeGenImpl implements IVerifyCodeGen {

    private static final int VALICATE_CODE_LENGTH = 4;

    private static final String[] FONT_TYPES = {"宋体", "新宋体", "黑体", "楷体", "隶书"};
    
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public VerifyCodeModel generate(int width, int height) {
        VerifyCodeModel verifyCodeModel = null;
        // 将流的初始化放到这里，就不需要手动关闭流了
        try (ByteArrayOutputStream bass = new ByteArrayOutputStream()) {
            String code = generate(width, height, bass);
            verifyCodeModel = new VerifyCodeModel();
            String requestOnce = UUID.randomUUID().toString();
            verifyCodeModel.setRequestOnce(requestOnce);
            verifyCodeModel.setImgBytes(bass.toByteArray());
            // 进行redis的保存
            stringRedisTemplate.opsForValue().set(requestOnce, code, 5, TimeUnit.MINUTES);
        } catch (IOException e) {
            log.error("验证码图片流异常", e);
        }
        return verifyCodeModel;
    }

    @Override
    public Result<Void> verifyCode(String requestOnce, String code) {
        if (!StringUtils.hasText(code)) {
            return Result.failBuild("验证码不能为空！");
        }
        if (!StringUtils.hasText(requestOnce)) {
            return Result.failBuild("验证码验证失败！");
        }
        String redisCode = stringRedisTemplate.opsForValue().get(requestOnce);
        if (!StringUtils.hasText(redisCode)) {
            return Result.failBuild(300, "验证码已过期，请刷新！");
        }
        if (!redisCode.equals(code)) {
            return Result.failBuild("验证码不正确！");
        }
        // 进行已用验证码的删除
        stringRedisTemplate.delete(requestOnce);
        return Result.successBuild();
    }

    /**
     * 生成随机字符串
     *
     * @param width        图片宽度
     * @param height       图片高度
     * @param outputStream 输出流
     * @return 随机字符串
     */
    private String generate(int width, int height, OutputStream outputStream) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        fillBackground(graphics, width, height);
        String randomStr = CustomRandomUtils.randomString(VALICATE_CODE_LENGTH);
        createCharacter(graphics, randomStr);
        graphics.dispose();
        try {
            // 设置JPEG格式，并写入流
            ImageIO.write(image, "JPEG", outputStream);
        } catch (IOException e) {
            log.error("验证码随机生成的图片写入流异常", e);
        }
        return randomStr;
    }

    /**
     * 随机背景生成
     *
     * @param graphics 图片对象
     * @param width    宽度
     * @param height   高度
     */
    private void fillBackground(Graphics graphics, int width, int height) {
        // 背景填充
        graphics.setColor(Color.white);
        // 设置矩形坐标x,y 为0
        graphics.fillRect(0, 0, width, height);
        // 加入干扰线条
        for (int i = 0; i < 8; i++) {
            // 设置数据颜色算法参数
            graphics.setColor(CustomRandomUtils.randomColor(40, 150));
            Random random = new Random();
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            graphics.drawLine(x, y, x1, y1);
        }
    }

    /**
     * 设置字体的通用方法
     *
     * @param graphics  图像抽象类
     * @param randomStr 随机字符串
     */
    private void createCharacter(Graphics graphics, String randomStr) {
        char[] charArray = randomStr.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            // 设置RGB颜色算法参数
            graphics.setColor(CustomRandomUtils.randomColor(50, 150));
            // 设置字体大小，类型
            graphics.setFont(new Font(FONT_TYPES[RandomUtils.nextInt(0, FONT_TYPES.length)], Font.BOLD, 26));
            // 设置x y 坐标
            graphics.drawString(String.valueOf(charArray[i]), 15 * i + 5, 19 + RandomUtils.nextInt(0, 8));
        }
    }
}
