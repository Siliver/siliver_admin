package com.siliver.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.siliver.admin.common.Result;
import com.siliver.admin.request.RoleChangeRequest;
import com.siliver.admin.request.RoleGroupRequest;
import com.siliver.admin.response.RoleGroupResponse;
import com.siliver.admin.response.RoleListResponse;
import com.siliver.admin.response.RoleManageListResponse;
import com.siliver.admin.service.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色接口
 *
 * @author siliver
 */
@Slf4j
@Tag(name = "RoleController", description = "角色相关")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    /**
     * 获取角色列表
     *
     * @param page     页码
     * @param pageSize 页大小
     * @return 角色分页列表
     */
    @Operation(description = "角色列表接口", summary = "角色相关")
    @GetMapping("/app/list")
    public Result<IPage<RoleListResponse>> getRoleList(
            @Parameter(name = "page", in = ParameterIn.QUERY, required = true, description = "页码") @RequestParam("page") int page,
            @Parameter(name = "pageSize", in = ParameterIn.QUERY, required = true, description = "页大小") @RequestParam("pageSize") int pageSize
    ) {
        return roleService.getRoleListService(page, pageSize);
    }

    /**
     * 角色管理列表接口
     *
     * @param page     页码
     * @param pageSize 页大小
     * @param roleCode 角色编号
     * @param roleName 角色名称
     * @return 角色列表
     */
    @Operation(description = "角色管理列表接口", summary = "角色相关")
    @GetMapping("/web/list")
    public Result<IPage<RoleManageListResponse>> getRoleManageList(
            @Parameter(name = "page", in = ParameterIn.QUERY, required = true, description = "页码") @RequestParam("page") int page,
            @Parameter(name = "pageSize", in = ParameterIn.QUERY, required = true, description = "页大小") @RequestParam("pageSize") int pageSize,
            @Parameter(name = "roleCode", in = ParameterIn.QUERY, description = "角色编号") @RequestParam(value = "roleCode", required = false) String roleCode,
            @Parameter(name = "roleName", in = ParameterIn.QUERY, description = "角色名称") @RequestParam(value = "roleName", required = false) String roleName
    ) {
        return roleService.getRoleManageListService(page, pageSize, roleCode, roleName);
    }

    /**
     * 角色管理变更接口
     *
     * @param change            操作类型
     * @param roleChangeRequest 变更参数
     * @return 变更结果
     */
    @Operation(description = "角色管理变更接口", summary = "角色相关")
    @PostMapping("/web/{change}")
    public Result<Void> changeRoleManage(
            @Parameter(name = "change", in = ParameterIn.PATH, required = true, description = "修改参数") @PathVariable("change") String change,
            @Parameter(name = "userCode", in = ParameterIn.HEADER, required = true, description = "修改参数") @RequestHeader("userCode") String userCode,
            @RequestBody RoleChangeRequest roleChangeRequest) {
        roleChangeRequest.setCreateUser(userCode);
        roleChangeRequest.setUpdateUser(userCode);
        return roleService.changeRoleManageService(change, roleChangeRequest);
    }

    /**
     * 角色分组接口
     *
     * @param roleId 角色ID
     * @return 分组列表
     */
    @Operation(description = "角色分组接口", summary = "菜单相关")
    @GetMapping("/group/list")
    public Result<List<RoleGroupResponse>> getRoleGroupList(
            @Parameter(name = "roleId", in = ParameterIn.QUERY, required = true, description = "角色ID") @RequestParam("roleId") int roleId
    ) {
        return roleService.getRoleGroupListService(roleId);
    }

    /**
     * 角色分组维护接口
     *
     * @param userCode         操作用户编号
     * @param roleGroupRequest 修改请求实体
     * @return 编辑结果
     */
    @Operation(description = "菜单角色维护接口", summary = "菜单相关")
    @PostMapping("/role/change")
    public Result<Void> changeRoleGroup(
            @Parameter(name = "userCode", in = ParameterIn.HEADER, required = true, description = "操作用户名称") @RequestHeader("userCode") String userCode,
            @RequestBody RoleGroupRequest roleGroupRequest
    ) {
        return roleService.changeRoleGroupService(userCode, roleGroupRequest);
    }
}
