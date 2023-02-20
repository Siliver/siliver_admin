package com.siliver.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siliver.admin.common.Result;
import com.siliver.admin.dto.MenuDto;
import com.siliver.admin.request.MenuChangeRequest;
import com.siliver.admin.request.MenuRoleRequest;
import com.siliver.admin.response.MenuManageListResponse;
import com.siliver.admin.response.MenuRoleListResponse;
import com.siliver.admin.response.UserMenuResponse;

import java.util.List;

/**
 * 菜单相关interface
 *
 * @author siliver
 */
public interface IMenuService extends IService<MenuDto> {

    /**
     * 获取用户菜单列表
     *
     * @param userId 用户ID
     * @return 用户菜单列表
     */
    Result<List<UserMenuResponse>> getUserMenuListService(int userId);

    /**
     * 菜单查询列表接口
     *
     * @param page     页码
     * @param pageSize 页大小
     * @param menuName 菜单名称
     * @return 菜单列表
     */
    Result<IPage<MenuManageListResponse>> getMenuManageListService(int page, int pageSize, String menuName);

    /**
     * 菜单管理变更接口
     *
     * @param change            操作类型
     * @param menuChangeRequest 变更参数
     * @return 变更结果
     */
    Result<Void> changeMenuManageService(String change, MenuChangeRequest menuChangeRequest);

    /**
     * 菜单角色列表查询接口
     *
     * @param menuId 菜单编号
     * @return 菜单对应的角色列表
     */
    Result<List<MenuRoleListResponse>> getMenuRoleListService(int menuId);

    /**
     * 菜单角色修改接口
     *
     * @param userCode        操作用户编号
     * @param menuRoleRequest 修改参数
     * @return 修改结果
     */
    Result<Void> changeMenuRoleService(String userCode, MenuRoleRequest menuRoleRequest);
}
