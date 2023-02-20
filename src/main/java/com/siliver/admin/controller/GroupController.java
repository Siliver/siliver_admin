package com.siliver.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.siliver.admin.common.Result;
import com.siliver.admin.request.GroupChangeRequest;
import com.siliver.admin.request.GroupUserRequest;
import com.siliver.admin.response.GroupListResponse;
import com.siliver.admin.response.GroupManageListResponse;
import com.siliver.admin.response.GroupUserListResponse;
import com.siliver.admin.service.IGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户组接口
 *
 * @author siliver
 */

@Tag(name = "GroupController", description = "用户组相关")
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    private final IGroupService groupService;

    /**
     * 用户组列表
     *
     * @param groupId 父级节点ID
     * @return 查询结果
     */
    @Operation(description = "用户组列表接口", summary = "用户组相关")
    @GetMapping("/app/list")
    public Result<List<GroupListResponse>> getGroupList(
            @Parameter(name = "groupId", in = ParameterIn.QUERY, required = true, description = "groupId：如果有，就查询其子节点；没有就查询所有") @RequestParam("groupId") Integer groupId
    ) {
        return groupService.getGroupListService(groupId);
    }

    /**
     * 用户组列表接口
     *
     * @param page      页码
     * @param pageSize  页大小
     * @param groupCode 用户组编号
     * @param groupName 用户组名称
     * @return 用户组列表
     */
    @Operation(description = "用户组管理列表接口", summary = "用户组相关")
    @GetMapping("/web/list")
    public Result<IPage<GroupManageListResponse>> getGroupManageList(
            @Parameter(name = "page", in = ParameterIn.QUERY, required = true, description = "页码") @RequestParam("page") int page,
            @Parameter(name = "pageSize", in = ParameterIn.QUERY, required = true, description = "页大小") @RequestParam("pageSize") int pageSize,
            @Parameter(name = "groupCode", in = ParameterIn.QUERY, description = "用户组编号") @RequestParam(value = "groupCode", required = false) String groupCode,
            @Parameter(name = "groupName", in = ParameterIn.QUERY, description = "用户组名称") @RequestParam(value = "groupName", required = false) String groupName
    ) {
        return groupService.getGroupManageListService(page, pageSize, groupCode, groupName);
    }


    /**
     * 用户组管理变更接口
     *
     * @param change             操作类型
     * @param groupChangeRequest 变更参数
     * @return 变更结果
     */
    @Operation(description = "用户组管理变更接口", summary = "用户组相关")
    @PostMapping("/web/{change}")
    public Result<Void> changeGroupManage(
            @Parameter(name = "change", in = ParameterIn.PATH, required = true, description = "修改参数") @PathVariable("change") String change,
            @Parameter(name = "userCode", in = ParameterIn.HEADER, required = true, description = "修改参数") @RequestHeader("userCode") String userCode,
            @RequestBody GroupChangeRequest groupChangeRequest) {
        groupChangeRequest.setCreateUser(userCode);
        groupChangeRequest.setUpdateUser(userCode);
        return groupService.changeGroupManageService(change, groupChangeRequest);
    }

    /**
     * 用户组用户列表查询接口
     *
     * @param groupId 用户组ID
     * @return 用户列表
     */
    @Operation(description = "用户组用户列表查询接口", summary = "用户组相关")
    @GetMapping("/role/list")
    public Result<List<GroupUserListResponse>> getGroupUserList(
            @Parameter(name = "groupId", in = ParameterIn.QUERY, required = true, description = "用户组ID") @RequestParam("groupId") int groupId
    ) {
        return groupService.getGroupUserListService(groupId);
    }

    /**
     * 用户组用户维护接口
     *
     * @param userCode         操作用户编号
     * @param groupUserRequest 修改请求实体
     * @return 编辑结果
     */
    @Operation(description = "用户组用户维护接口", summary = "用户组相关")
    @PostMapping("/role/change")
    public Result<Void> changeGroupUser(
            @Parameter(name = "userCode", in = ParameterIn.HEADER, required = true, description = "操作用户名称") @RequestHeader("userCode") String userCode,
            @RequestBody GroupUserRequest groupUserRequest
    ) {
        return groupService.changeGroupUserService(userCode, groupUserRequest);
    }
}
