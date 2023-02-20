package com.siliver.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siliver.admin.common.Result;
import com.siliver.admin.dto.GroupDto;
import com.siliver.admin.request.GroupChangeRequest;
import com.siliver.admin.request.GroupUserRequest;
import com.siliver.admin.response.GroupListResponse;
import com.siliver.admin.response.GroupManageListResponse;
import com.siliver.admin.response.GroupUserListResponse;

import java.util.List;

/**
 * 用户组逻辑服务
 *
 * @author siliver
 */
public interface IGroupService extends IService<GroupDto> {

    /**
     * 用户组列表
     *
     * @param groupId 父级节点ID
     * @return 查询结果
     */
    Result<List<GroupListResponse>> getGroupListService(Integer groupId);

    /**
     * 用户组列表接口
     *
     * @param page      页码
     * @param pageSize  页大小
     * @param groupCode 用户组编号
     * @param groupName 用户组名称
     * @return 用户组列表
     */
    Result<IPage<GroupManageListResponse>> getGroupManageListService(int page, int pageSize, String groupCode, String groupName);

    /**
     * 角色管理变更接口
     *
     * @param change             操作类型
     * @param groupChangeRequest 变更参数
     * @return 变更结果
     */
    Result<Void> changeGroupManageService(String change, GroupChangeRequest groupChangeRequest);

    /**
     * 用户组用户列表查询接口
     *
     * @param groupId 用户组ID
     * @return 用户列表
     */
    Result<List<GroupUserListResponse>> getGroupUserListService(int groupId);

    /**
     * 用户组用户维护接口
     *
     * @param userCode         操作用户编号
     * @param groupUserRequest 修改请求实体
     * @return 编辑结果
     */
    Result<Void> changeGroupUserService(String userCode, GroupUserRequest groupUserRequest);
}
