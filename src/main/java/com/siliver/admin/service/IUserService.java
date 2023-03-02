package com.siliver.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siliver.admin.common.Result;
import com.siliver.admin.dto.UserDto;
import com.siliver.admin.request.UserInfoRequest;
import com.siliver.admin.request.UserManageRequest;
import com.siliver.admin.response.LoginResponse;
import com.siliver.admin.response.UserInfoResponse;
import com.siliver.admin.response.UserManageListResponse;

/**
 * 用户相关方法
 *
 * @author siliver
 */
public interface IUserService extends IService<UserDto> {

    /**
     * 登录方法
     *
     * @param password 密码
     * @param userDto  用户信息
     * @return 登录返回
     */
    Result<LoginResponse> userLogin(String password, UserDto userDto, String ip);

    /**
     * 用户状态验证
     *
     * @param username 用户姓名
     * @return 验证结果
     */
    Result<UserDto> judgeUserStatus(String username);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    Result<UserInfoResponse> getUserInfoService(String userId);

    /**
     * 获取用户信息列表
     *
     * @param page     页码
     * @param pageSize 分页大小
     * @param username 用户账号
     * @param nickName 用户昵称
     * @return 用户列表
     */
    Result<IPage<UserManageListResponse>> getUserManageListService(int page, int pageSize, String username, String nickName);

    /**
     * 更新用户信息服务
     *
     * @param userInfoRequest 用户信息更新请求
     * @return 结果
     */
    Result<Void> updateUserInfoService(UserInfoRequest userInfoRequest);

    /**
     * 用户信息维护
     *
     * @param change            操作类型
     * @param userManageRequest 变更参数
     * @return 变更结果
     */
    Result<Void> changeUserManageService(String change, UserManageRequest userManageRequest);

}
