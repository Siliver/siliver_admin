package com.siliver.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.siliver.admin.dto.RoleDto;
import com.siliver.admin.response.RoleGroupResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色mapper
 *
 * @author siliver
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleDto> {

    /**
     * 获取用户的角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleDto> getRolesByUserAndGroup(@Param("userId") Integer userId);

    /**
     * 根据角色ID获取角色下的分组
     *
     * @param roleId 角色ID
     * @return 分组列表
     */
    List<RoleGroupResponse> getGroupsByRoleIdMapper(@Param("roleId") int roleId);
}
