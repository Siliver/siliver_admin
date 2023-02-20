package com.siliver.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siliver.admin.common.Result;
import com.siliver.admin.dao.GroupMapper;
import com.siliver.admin.dto.GroupDto;
import com.siliver.admin.dto.UserGroupDto;
import com.siliver.admin.neum.CommonValue;
import com.siliver.admin.request.GroupChangeRequest;
import com.siliver.admin.request.GroupUserRequest;
import com.siliver.admin.response.GroupListResponse;
import com.siliver.admin.response.GroupManageListResponse;
import com.siliver.admin.response.GroupUserListResponse;
import com.siliver.admin.service.IGroupService;
import com.siliver.admin.service.IUserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.siliver.admin.neum.CommonValue.CODE_LENGTH;
import static com.siliver.admin.neum.CommonValue.NAME_LENGTH;

/**
 * 用户组服务实现
 *
 * @author siliver
 */
@RequiredArgsConstructor
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDto> implements IGroupService {

    private final CommonServiceImpl<GroupChangeRequest, GroupDto> commonService;

    private final IUserGroupService userGroupService;

    @Override
    public Result<List<GroupListResponse>> getGroupListService(Integer groupId) {
        // 判断查询类型
        List<GroupDto> groupDtos = list();
        // 进行树形结构的拼接，和无用节点的抛出
        return Result.successBuild(findChildNode(groupDtos, Objects.isNull(groupId) ? 0 : groupId));
    }

    @Override
    public Result<IPage<GroupManageListResponse>> getGroupManageListService(int page, int pageSize, String groupCode, String groupName) {
        // 进行列表查询
        LambdaQueryWrapper<GroupDto> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.hasText(groupCode)) {
            lambdaQueryWrapper.like(GroupDto::getGroupCode, groupCode);
        }
        if (StringUtils.hasText(groupName)) {
            lambdaQueryWrapper.like(GroupDto::getGroupName, groupName);
        }
        // 进行排序
        lambdaQueryWrapper.orderByDesc(GroupDto::getUpdateTime);
        // 进行字段查询拼接
        lambdaQueryWrapper.select(GroupDto::getId, GroupDto::getCreateTime, GroupDto::getUpdateTime, GroupDto::getCreateUser, GroupDto::getUpdateUser, GroupDto::getDeleteFlag, GroupDto::getGroupCode, GroupDto::getGroupName);
        // 进行分页条件拼接
        IPage<GroupDto> temp = new Page<>(page, pageSize);
        temp = page(temp, lambdaQueryWrapper);
        if (Objects.isNull(temp) || CollectionUtils.isEmpty(temp.getRecords())) {
            return Result.failBuild();
        }
        IPage<GroupManageListResponse> responseIpage = new Page<>();
        List<GroupManageListResponse> groupManageListResponses = new ArrayList<>(responseIpage.getRecords().size());
        temp.getRecords().forEach(t -> {
            GroupManageListResponse groupManageListResponse = new GroupManageListResponse();
            BeanUtils.copyProperties(t, groupManageListResponse);
            groupManageListResponses.add(groupManageListResponse);
        });
        responseIpage.setCurrent(temp.getCurrent());
        responseIpage.setTotal(temp.getTotal());
        responseIpage.setSize(temp.getSize());
        responseIpage.setRecords(groupManageListResponses);
        return Result.successBuild(responseIpage);
    }

    /**
     * 角色管理变更接口
     *
     * @param change             操作类型
     * @param groupChangeRequest 变更参数
     * @return 变更结果
     */
    @Override
    public Result<Void> changeGroupManageService(String change, GroupChangeRequest groupChangeRequest) {
        // 进行非空判断
        if (CommonValue.UPDATE_STRING.equals(change)) {
            if (!StringUtils.hasText(groupChangeRequest.getGroupCode())) {
                return Result.failBuild("用户组编号不能为空！");
            }
            if (1 > groupChangeRequest.getGroupCode().length() || CODE_LENGTH < groupChangeRequest.getGroupCode().length()) {
                return Result.failBuild("用户组编号长度为1-36！");
            }
            if (!StringUtils.hasText(groupChangeRequest.getGroupName())) {
                return Result.failBuild("用户组名称不能为空！");
            }
            if (1 > groupChangeRequest.getGroupName().length() || NAME_LENGTH < groupChangeRequest.getGroupName().length()) {
                return Result.failBuild("用户组名称长度为1-50！");
            }
        }
        return commonService.changeDictionaryService(change, groupChangeRequest, new GroupDto());
    }

    /**
     * 用户组用户列表查询接口
     *
     * @param groupId 用户组ID
     * @return 用户列表
     */
    @Override
    public Result<List<GroupUserListResponse>> getGroupUserListService(int groupId) {
        List<GroupUserListResponse> groupUserListResponses = baseMapper.getGroupUserListMapper(groupId);
        return Result.successBuild(groupUserListResponses);
    }

    /**
     * 用户组用户维护接口
     *
     * @param userCode         操作用户编号
     * @param groupUserRequest 修改请求实体
     * @return 编辑结果
     */
    @Override
    public Result<Void> changeGroupUserService(String userCode, GroupUserRequest groupUserRequest) {
        // 进行关联查询
        List<UserGroupDto> userGroupList = userGroupService.list(Wrappers.<UserGroupDto>lambdaQuery().eq(UserGroupDto::getGroupId, groupUserRequest.getGroupId()).in(UserGroupDto::getUserId, groupUserRequest.getUserIds()).isNotNull(UserGroupDto::getDeleteFlag));
        // 进行更新拼接
        List<UserGroupDto> temp = new ArrayList<>(groupUserRequest.getUserIds().size());
        for (Integer userId : groupUserRequest.getUserIds()) {
            Optional<UserGroupDto> userGroupDtoOptional = userGroupList.stream().filter(t -> Objects.equals(userId, t.getUserId()) && Objects.equals(groupUserRequest.getGroupId(), t.getGroupId())).findFirst();
            if (userGroupDtoOptional.isPresent()) {
                UserGroupDto userGroupDto = userGroupDtoOptional.get();
                userGroupDto.setDeleteFlag(0);
                userGroupDto.setUpdateUser(userCode);
                temp.add(userGroupDto);
            } else {
                UserGroupDto userGroupDto = new UserGroupDto();
                userGroupDto.setCreateUser(userCode);
                userGroupDto.setUpdateUser(userCode);
                userGroupDto.setGroupId(groupUserRequest.getGroupId());
                userGroupDto.setUserId(userId);
                temp.add(userGroupDto);
            }
        }
        // 进行批量更新
        boolean updateFlag = userGroupService.saveBatch(temp);
        return updateFlag ? Result.successBuild() : Result.failBuild();
    }

    /**
     * 进行节点树的拼接
     *
     * @param groupDtos 用户组数据
     * @param begin     开始index
     * @return 用户组结构
     */
    private List<GroupListResponse> findChildNode(List<GroupDto> groupDtos, int begin) {
        // 查询根节点
        return groupDtos.stream().filter(t -> t.getSuperId() == begin).map(t -> {
            GroupListResponse groupListResponse = new GroupListResponse();
            groupListResponse.setGroupCode(t.getGroupCode());
            groupListResponse.setGroupName(t.getGroupName());
            // 进行子节点的寻找
            groupListResponse.setChildren(findChildNode(groupDtos, t.getId()));
            return groupListResponse;
        }).toList();
    }
}
