package com.siliver.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.siliver.admin.dto.MenuDto;
import com.siliver.admin.response.MenuRoleListResponse;
import com.siliver.admin.response.UserMenuResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单管理mapper
 *
 * @author siliver
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuDto> {

    /**
     * 获取用户菜单列表
     *
     * @param userId 用户编号
     * @return 用户菜单列表
     */
    List<UserMenuResponse> getUserMenuMapper(@Param("userId") int userId);

    /**
     * 获取角色列表
     *
     * @param menuId 菜单ID
     * @return 角色列表
     */
    List<MenuRoleListResponse> getRolesByMenuIdMapper(@Param("menuId") int menuId);
}
