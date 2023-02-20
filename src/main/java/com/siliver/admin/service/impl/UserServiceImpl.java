package com.siliver.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siliver.admin.common.Result;
import com.siliver.admin.dao.UserMapper;
import com.siliver.admin.document.LoginLogDoc;
import com.siliver.admin.dto.UserDto;
import com.siliver.admin.request.LoginRequest;
import com.siliver.admin.response.LoginResponse;
import com.siliver.admin.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDto> implements IUserService {

    /**
     * 用户锁定键值后缀
     */
    private static final String USER_LOCK = "_lock";
    /**
     * security中的工具类
     */
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    /**
     * redis操作类
     */
    private final StringRedisTemplate stringRedisTemplate;
    /**
     * mongo操作类
     */
    private final MongoTemplate mongoTemplate;

    @Override
    public Result<LoginResponse> userLogin(LoginRequest loginRequest, UserDto userDto) {
        // 进行密码的匹配
        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), userDto.getPassword())) {
            // 进行锁定次数的记录
            if (userDto.getLockCount() < 1) {
                stringRedisTemplate.opsForValue().set(userDto.getUsername() + USER_LOCK, new Date().toString(), 10, TimeUnit.MINUTES);
                return Result.failBuild("验证码输入错误5次，账户已经锁定10分钟！");
            }
            update(Wrappers.<UserDto>lambdaUpdate().eq(UserDto::getId, userDto.getId()).set(UserDto::getLockCount, userDto.getLockCount() - 1));
            return Result.failBuild(String.format("用户密码不正确！，还有%s次输入机会！", userDto.getLockCount() - 1));
        }
        // 记录登录时间
        update(Wrappers.<UserDto>lambdaUpdate().eq(UserDto::getId, userDto.getId()).set(UserDto::getLastLoginTime, LocalDateTime.now()));
        // 进行mongodb的登录日志添加
        LoginLogDoc loginLogDoc = new LoginLogDoc();
        loginLogDoc.setUsername(userDto.getUsername());
        loginLogDoc.setLoginTime(LocalDateTime.now());
        mongoTemplate.insert(loginLogDoc);
        // 返回结果
        return Result.successBuild();
    }

    @Override
    public Result<UserDto> judgeUserStatus(String username) {
        // 进行用户查询
        LambdaQueryWrapper<UserDto> lambdaQueryWrapper = Wrappers.<UserDto>lambdaQuery()
                .eq(UserDto::getUsername, username)
                .select(UserDto.class, t -> true);
        UserDto userDto = getOne(lambdaQueryWrapper);
        if (Objects.isNull(userDto)) {
            return Result.failBuild("用户查询失败！");
        }
        // 查询锁定信息
        String lockFlag = stringRedisTemplate.opsForValue().get(userDto.getUsername() + USER_LOCK);
        if (StringUtils.hasText(lockFlag)) {
            // 进行长锁定的判断
            Long lockTime = stringRedisTemplate.getExpire(userDto.getUsername() + USER_LOCK);
            if (Objects.nonNull(lockTime)) {
                userDto.setExpiration(lockTime);
            }
            return new Result<>(300, "该账户已经锁定", userDto);
        } else {
            update(Wrappers.<UserDto>lambdaUpdate().eq(UserDto::getId, userDto.getId()).set(UserDto::getLockCount, 6).set(UserDto::isAccountNonLocked, false).set(UserDto::getUnlockTime, null));
        }
        // 进行用户状态的判断
        if (!userDto.isAccountNonExpired()) {
            return Result.failBuild("该用户已经过期，请联系管理员进行续期！");
        }
        return Result.successBuild(userDto);
    }
}
