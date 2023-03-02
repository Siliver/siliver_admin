package com.siliver.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siliver.admin.common.Result;
import com.siliver.admin.dao.UserMapper;
import com.siliver.admin.document.LoginLogDoc;
import com.siliver.admin.dto.GroupDto;
import com.siliver.admin.dto.RoleDto;
import com.siliver.admin.dto.UserDto;
import com.siliver.admin.dto.UserExtendDto;
import com.siliver.admin.request.UserInfoRequest;
import com.siliver.admin.request.UserManageRequest;
import com.siliver.admin.response.LoginResponse;
import com.siliver.admin.response.UserInfoResponse;
import com.siliver.admin.response.UserManageListResponse;
import com.siliver.admin.service.IUserExtendService;
import com.siliver.admin.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.siliver.admin.neum.CommonValue.UNKNOWN_SEX_TYPE;
import static com.siliver.admin.neum.ResultCode.LOCK_CODE;

/**
 * 用户服务实现
 *
 * @author siliver
 */
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    /**
     * redis操作类
     */
    private final StringRedisTemplate stringRedisTemplate;
    /**
     * mongo操作类
     */
    private final MongoTemplate mongoTemplate;

    private final IUserExtendService userExtendService;

    private final CommonServiceImpl<UserManageRequest, UserExtendDto> commonService;

    private final Ip2regionSearcher regionSearcher;

    @Override
    public Result<LoginResponse> userLogin(String password, UserDto userDto, String ip) {
        // 进行密码的匹配
        if (!bCryptPasswordEncoder.matches(password, userDto.getPassword())) {
            // 进行锁定次数的记录
            if (userDto.getLockCount() < 1) {
                stringRedisTemplate.opsForValue().set(userDto.getUsername() + USER_LOCK, new Date().toString(), 10, TimeUnit.MINUTES);
                return Result.failBuild("验证码输入错误5次，账户已经锁定10分钟！");
            }
            update(Wrappers.<UserDto>lambdaUpdate().eq(UserDto::getId, userDto.getId()).set(UserDto::getLockCount, userDto.getLockCount() - 1));
            return Result.failBuild(String.format("用户密码不正确！，还有%s次输入机会！", userDto.getLockCount() - 1));
        }
        // 记录登录时间
        update(Wrappers.<UserDto>lambdaUpdate().eq(UserDto::getId, userDto.getId()).set(UserDto::getLastLoginTime, LocalDateTime.now()).set(UserDto::getLockCount, 5));
        // 进行mongodb的登录日志添加
        LoginLogDoc loginLogDoc = new LoginLogDoc();
        loginLogDoc.setUsername(userDto.getUsername());
        loginLogDoc.setLoginTime(LocalDateTime.now());
        if (StringUtils.hasText(ip)){
            loginLogDoc.setIp(ip);
            loginLogDoc.setIpRegion(regionSearcher.getAddressAndIsp(ip));
        }
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
            return Result.failBuild("账号不正确！");
        }
        // 查询锁定信息
        String lockFlag = stringRedisTemplate.opsForValue().get(userDto.getUsername() + USER_LOCK);
        if (StringUtils.hasText(lockFlag)) {
            // 进行长锁定的判断
            Long lockTime = stringRedisTemplate.getExpire(userDto.getUsername() + USER_LOCK);
            if (Objects.nonNull(lockTime)) {
                userDto.setExpiration(lockTime);
            }
            return new Result<>(LOCK_CODE.getCode(), "该账户已经锁定", userDto);
        } else {
            if (0 == userDto.getLockCount()) {
                update(Wrappers.<UserDto>lambdaUpdate().eq(UserDto::getId, userDto.getId()).set(UserDto::getLockCount, 5).set(UserDto::isAccountNonExpired, true).set(UserDto::getUnlockTime, null));
            }
        }
        // 进行用户状态的判断
        if (!userDto.isAccountNonExpired()) {
            return Result.failBuild("该用户已经过期，请联系管理员进行续期！");
        }
        return Result.successBuild(userDto);
    }

    @Override
    public Result<UserInfoResponse> getUserInfoService(String userId) {
        UserDto userDto = getById(userId);
        List<GroupDto> groupDtos = baseMapper.getUserGroupsMapper(Integer.valueOf(userId));
        List<RoleDto> roleDtos = baseMapper.getUserRolesMapper(groupDtos.stream().map(GroupDto::getId).toList());
        UserExtendDto userExtendDto = userExtendService.getOne(Wrappers.<UserExtendDto>lambdaQuery().eq(UserExtendDto::getUserId, userId));
        // 进行用户拼接
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setId(userDto.getId());
        userInfoResponse.setUsername(userDto.getUsername());
        userInfoResponse.setGroupNames(groupDtos.stream().map(GroupDto::getGroupName).toList());
        userInfoResponse.setRoleNames(roleDtos.stream().map(RoleDto::getRoleName).toList());
        userInfoResponse.setNickName(userExtendDto.getNickName());
        userInfoResponse.setSex(userExtendDto.getSex());
        userInfoResponse.setSignature(userExtendDto.getSignature());
        return Result.successBuild(userInfoResponse);
    }

    /**
     * 获取用户信息列表
     *
     * @param page     页码
     * @param pageSize 分页大小
     * @param username 用户账号
     * @param nickName 用户昵称
     * @return 用户列表
     */
    @Override
    public Result<IPage<UserManageListResponse>> getUserManageListService(int page, int pageSize, String username, String nickName) {
        IPage<UserManageListResponse> page1 = new Page<>(page, pageSize);
        IPage<UserManageListResponse> result = baseMapper.getUserManageListMapper(page1, username, nickName);
        return Result.successBuild(result);
    }

    /**
     * 更新用户信息服务
     *
     * @param userInfoRequest 用户信息更新请求
     * @return 结果
     */
    @Override
    public Result<Void> updateUserInfoService(UserInfoRequest userInfoRequest) {
        UserExtendDto userExtendDto = userExtendService.getOne(Wrappers.<UserExtendDto>lambdaQuery().eq(UserExtendDto::getUserId, userInfoRequest.getUserId()));
        if (Objects.isNull(userExtendDto)) {
            return Result.failBuild("用户不存在！");
        }
        // 更新用户信息
        userExtendDto.setNickName(userInfoRequest.getNickName());
        if (Objects.isNull(userInfoRequest.getSex())) {
            userExtendDto.setSex(UNKNOWN_SEX_TYPE);
        } else {
            userExtendDto.setSex(userInfoRequest.getSex());
        }
        userExtendDto.setSignature(userInfoRequest.getSignature());
        userExtendDto.updateById();
        return Result.successBuild();
    }

    /**
     * 用户信息维护
     *
     * @param change            操作类型
     * @param userManageRequest 变更参数
     * @return 变更结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Void> changeUserManageService(String change, UserManageRequest userManageRequest) {
        commonService.changeDictionaryService(change, userManageRequest, new UserExtendDto());
        if (Objects.nonNull(userManageRequest.getAccountNonLocked())) {
            update(Wrappers.<UserDto>lambdaUpdate().eq(UserDto::getId, userManageRequest.getId()).set(UserDto::isAccountNonLocked, userManageRequest.getAccountNonLocked()));
        }
        return Result.successBuild();
    }

}
