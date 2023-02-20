package com.siliver.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.siliver.admin.dto.GroupDto;
import com.siliver.admin.response.GroupUserListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户组mapper
 *
 * @author siliver
 */
@Mapper
public interface GroupMapper extends BaseMapper<GroupDto> {

    /**
     * 获取用户的用户组
     *
     * @param userIds 用户编号集合
     * @return 用户组
     */
    List<GroupDto> getGroupsByUser(@Param("userIds") List<Integer> userIds);

    /**
     * 用户组用户列表
     *
     * @param groupId 用户组编号
     * @return 用户列表
     */
    List<GroupUserListResponse> getGroupUserListMapper(@Param("groupId") Integer groupId);
}
