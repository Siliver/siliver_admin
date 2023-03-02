package com.siliver.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siliver.admin.common.Result;
import com.siliver.admin.dto.RoleDto;
import com.siliver.admin.request.RoleChangeRequest;
import com.siliver.admin.request.RoleGroupRequest;
import com.siliver.admin.response.RoleGroupResponse;
import com.siliver.admin.response.RoleListResponse;
import com.siliver.admin.response.RoleManageListResponse;

import java.util.List;

/**
 * 角色服务
 *
 * @author siliver
 */
public interface IRoleService extends IService<RoleDto> {

    /**
     * 根据用户ID查询角色信息
     *
     * @param userId 用户ID
     * @return 用户的角色名称
     */
    List<RoleDto> getRolesByUserAndGroupService(Integer userId);

    /**
     * 获取角色列表
     *
     * @return 角色分页列表
     */
    Result<List<RoleListResponse>> getRoleListService();


    /**
     * 角色管理列表接口
     *
     * @param page     页码
     * @param pageSize 页大小
     * @param roleCode 角色编号
     * @param roleName 角色名称
     * @return 角色列表
     */
    Result<IPage<RoleManageListResponse>> getRoleManageListService(int page, int pageSize, String roleCode, String roleName);

    /**
     * 角色管理变更接口
     *
     * @param change            操作类型
     * @param roleChangeRequest 变更参数
     * @return 变更结果
     */
    Result<Void> changeRoleManageService(String change, RoleChangeRequest roleChangeRequest);

    /**
     * 角色分组接口
     *
     * @param roleId 角色ID
     * @return 分组列表
     */
    Result<List<RoleGroupResponse>> getRoleGroupListService(int roleId);

    /**
     * 角色分组维护接口
     *
     * @param userCode         操作用户编号
     * @param roleGroupRequest 修改请求实体
     * @return 编辑结果
     */
    Result<Void> changeRoleGroupService(String userCode, RoleGroupRequest roleGroupRequest);
}
