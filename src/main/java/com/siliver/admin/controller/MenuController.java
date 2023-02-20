package com.siliver.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.siliver.admin.common.Result;
import com.siliver.admin.request.MenuChangeRequest;
import com.siliver.admin.request.MenuRoleRequest;
import com.siliver.admin.response.MenuManageListResponse;
import com.siliver.admin.response.MenuRoleListResponse;
import com.siliver.admin.response.UserMenuResponse;
import com.siliver.admin.service.IMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单相关 controller
 *
 * @author siliver
 */
@Slf4j
@Tag(name = "MenuController", description = "菜单相关")
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final IMenuService menuService;

    /**
     * 获取用户菜单列表
     *
     * @param userId 用户编号
     * @return 菜单列表
     */
    @Operation(description = "获取用户菜单列表", summary = "菜单相关")
    @GetMapping("/app/list")
    public Result<List<UserMenuResponse>> getUserMenuList(@RequestHeader("userId") int userId) {
        return menuService.getUserMenuListService(userId);
    }

    /**
     * 菜单管理列表接口
     *
     * @param page     页码
     * @param pageSize 页大小
     * @param menuName 菜单名称
     * @return 菜单列表
     */
    @Operation(description = "菜单管理列表接口", summary = "菜单相关")
    @GetMapping("/web/list")
    public Result<IPage<MenuManageListResponse>> getMenuManageList(
            @Parameter(name = "page", in = ParameterIn.QUERY, required = true, description = "页码") @RequestParam("page") int page,
            @Parameter(name = "pageSize", in = ParameterIn.QUERY, required = true, description = "页大小") @RequestParam("pageSize") int pageSize,
            @Parameter(name = "menuName", in = ParameterIn.QUERY, description = "角色编号") @RequestParam(value = "menuName", required = false) String menuName
    ) {
        return menuService.getMenuManageListService(page, pageSize, menuName);
    }

    /**
     * 菜单管理变更接口
     *
     * @param change            操作类型
     * @param menuChangeRequest 变更参数
     * @return 变更结果
     */
    @Operation(description = "菜单管理变更接口", summary = "菜单相关")
    @PostMapping("/web/{change}")
    public Result<Void> changeMenuManage(
            @Parameter(name = "change", in = ParameterIn.PATH, required = true, description = "修改参数") @PathVariable("change") String change,
            @Parameter(name = "userCode", in = ParameterIn.HEADER, required = true, description = "操作用户名称") @RequestHeader("userCode") String userCode,
            @RequestBody MenuChangeRequest menuChangeRequest
    ) {
        menuChangeRequest.setCreateUser(userCode);
        menuChangeRequest.setUpdateUser(userCode);
        return menuService.changeMenuManageService(change, menuChangeRequest);
    }

    /**
     * 菜单角色接口
     *
     * @param menuId 菜单ID
     * @return 菜单角色列表
     */
    @Operation(description = "菜单角色接口", summary = "菜单相关")
    @GetMapping("/role/list")
    public Result<List<MenuRoleListResponse>> getMenuRoleList(
            @Parameter(name = "menuId", in = ParameterIn.QUERY, required = true, description = "菜单ID") @RequestParam("menuId") int menuId
    ) {
        return menuService.getMenuRoleListService(menuId);
    }

    /**
     * 菜单角色维护接口
     *
     * @param userCode        操作用户编号
     * @param menuRoleRequest 修改请求实体
     * @return 编辑结果
     */
    @Operation(description = "菜单角色维护接口", summary = "菜单相关")
    @PostMapping("/role/change")
    public Result<Void> changeMenuRole(
            @Parameter(name = "userCode", in = ParameterIn.HEADER, required = true, description = "操作用户名称") @RequestHeader("userCode") String userCode,
            @RequestBody MenuRoleRequest menuRoleRequest
    ) {
        return menuService.changeMenuRoleService(userCode, menuRoleRequest);
    }
}
