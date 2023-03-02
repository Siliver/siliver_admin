package com.siliver.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.siliver.admin.dto.GroupDto;
import com.siliver.admin.dto.RoleDto;
import com.siliver.admin.dto.UserDto;
import com.siliver.admin.response.UserManageListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户mapper
 *
 * @author siliver
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDto> {

    /**
     * 获取用户的用户组
     *
     * @param userId 用户ID
     * @return 用户组集合
     */
    List<GroupDto> getUserGroupsMapper(@Param("userId") Integer userId);

    /**
     * 获取用户角色列表
     *
     * @param groupIds 用户用户组集合
     * @return 用户角色列表
     */
    List<RoleDto> getUserRolesMapper(@Param("groupIds") List<Integer> groupIds);

    /**
     * 用户信息管理列表
     *
     * @param page     页码
     * @param username 用户账号
     * @param nickName 用户昵称
     * @return 用户列表
     */
    IPage<UserManageListResponse> getUserManageListMapper(IPage<UserManageListResponse> page, @Param("username") String username, @Param("nickName") String nickName);
}
