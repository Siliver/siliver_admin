package com.siliver.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siliver.admin.common.Result;
import com.siliver.admin.dao.RoleMapper;
import com.siliver.admin.dto.RoleDto;
import com.siliver.admin.dto.RoleGroupDto;
import com.siliver.admin.neum.CommonValue;
import com.siliver.admin.request.RoleChangeRequest;
import com.siliver.admin.request.RoleGroupRequest;
import com.siliver.admin.response.RoleGroupResponse;
import com.siliver.admin.response.RoleListResponse;
import com.siliver.admin.response.RoleManageListResponse;
import com.siliver.admin.service.IRoleGroupService;
import com.siliver.admin.service.IRoleService;
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
 * 角色具体服务实现类
 *
 * @author siliver
 */
@RequiredArgsConstructor
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDto> implements IRoleService {

    private final CommonServiceImpl<RoleChangeRequest, RoleDto> commonService;

    private final IRoleGroupService roleGroupService;

    @Override
    public List<RoleDto> getRolesByUserAndGroupService(Integer userId) {
        return this.getBaseMapper().getRolesByUserAndGroup(userId);
    }

    @Override
    public Result<IPage<RoleListResponse>> getRoleListService(int page, int pageSize) {
        IPage<RoleDto> page1 = new Page<>();
        // 拼接查询条件
        LambdaQueryWrapper<RoleDto> lambdaQueryWrapper = Wrappers.<RoleDto>lambdaQuery()
                .select(RoleDto::getRoleCode, RoleDto::getRoleName)
                .orderByDesc(RoleDto::getCreateTime);
        page1 = page(page1, lambdaQueryWrapper);
        // 进行返回值转换
        if (Objects.isNull(page1) || CollectionUtils.isEmpty(page1.getRecords())) {
            return Result.successBuild();
        }
        // 进行类型转换
        List<RoleListResponse> roleListResponses = new ArrayList<>(page1.getRecords().size());
        page1.getRecords().forEach(t -> {
            RoleListResponse roleListResponse = new RoleListResponse();
            BeanUtils.copyProperties(t, roleListResponse);
            roleListResponses.add(roleListResponse);
        });
        // 进行返回结构的拼接
        IPage<RoleListResponse> page2 = new Page<>();
        page2.setCurrent(page1.getCurrent());
        page2.setSize(page1.getSize());
        page2.setTotal(page1.getTotal());
        page2.setRecords(roleListResponses);
        return Result.successBuild(page2);
    }

    @Override
    public Result<IPage<RoleManageListResponse>> getRoleManageListService(int page, int pageSize, String roleCode, String roleName) {
        // 进行列表查询
        LambdaQueryWrapper<RoleDto> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.hasText(roleCode)) {
            lambdaQueryWrapper.like(RoleDto::getRoleCode, roleCode);
        }
        if (StringUtils.hasText(roleName)) {
            lambdaQueryWrapper.like(RoleDto::getRoleName, roleName);
        }
        // 进行排序
        lambdaQueryWrapper.orderByDesc(RoleDto::getUpdateTime);
        // 进行字段查询拼接
        lambdaQueryWrapper.select(RoleDto::getId, RoleDto::getCreateTime, RoleDto::getUpdateTime, RoleDto::getCreateUser, RoleDto::getUpdateUser, RoleDto::getDeleteFlag, RoleDto::getRoleCode, RoleDto::getRoleName);
        // 进行分页条件拼接
        IPage<RoleDto> temp = new Page<>(page, pageSize);
        temp = page(temp, lambdaQueryWrapper);
        if (Objects.isNull(temp) || CollectionUtils.isEmpty(temp.getRecords())) {
            return Result.failBuild();
        }
        IPage<RoleManageListResponse> responseIpage = new Page<>();
        List<RoleManageListResponse> roleManageListResponses = new ArrayList<>(responseIpage.getRecords().size());
        temp.getRecords().forEach(t -> {
            RoleManageListResponse roleManageListResponse = new RoleManageListResponse();
            BeanUtils.copyProperties(t, roleManageListResponse);
            roleManageListResponses.add(roleManageListResponse);
        });
        responseIpage.setCurrent(temp.getCurrent());
        responseIpage.setTotal(temp.getTotal());
        responseIpage.setSize(temp.getSize());
        responseIpage.setRecords(roleManageListResponses);
        return Result.successBuild(responseIpage);
    }

    @Override
    public Result<Void> changeRoleManageService(String change, RoleChangeRequest roleChangeRequest) {
        // 进行非空判断
        if (CommonValue.UPDATE_STRING.equals(change)) {
            if (!StringUtils.hasText(roleChangeRequest.getRoleCode())) {
                return Result.failBuild("角色编号不能为空！");
            }
            if (1 > roleChangeRequest.getRoleCode().length() || CODE_LENGTH < roleChangeRequest.getRoleCode().length()) {
                return Result.failBuild("角色编号长度为1-36！");
            }
            if (!StringUtils.hasText(roleChangeRequest.getRoleName())) {
                return Result.failBuild("角色名称不能为空！");
            }
            if (1 > roleChangeRequest.getRoleName().length() || NAME_LENGTH < roleChangeRequest.getRoleName().length()) {
                return Result.failBuild("角色名称长度为1-50！");
            }
        }
        return commonService.changeDictionaryService(change, roleChangeRequest, new RoleDto());
    }

    /**
     * 角色分组接口
     *
     * @param roleId 角色ID
     * @return 分组列表
     */
    @Override
    public Result<List<RoleGroupResponse>> getRoleGroupListService(int roleId) {
        // 进行角色下分组的查询
        List<RoleGroupResponse> roleGroupResponses = baseMapper.getGroupsByRoleIdMapper(roleId);
        return Result.successBuild(roleGroupResponses);
    }

    /**
     * 菜单管理变更接口
     *
     * @param userCode         操作类型
     * @param roleGroupRequest 变更参数
     * @return 变更结果
     */
    @Override
    public Result<Void> changeRoleGroupService(String userCode, RoleGroupRequest roleGroupRequest) {
        // 进行关联查询
        List<RoleGroupDto> roleGroupList = roleGroupService.list(Wrappers.<RoleGroupDto>lambdaQuery().eq(RoleGroupDto::getRoleId, roleGroupRequest.getRoleId()).in(RoleGroupDto::getGroupId, roleGroupRequest.getGroupIds()).isNotNull(RoleGroupDto::getDeleteFlag));
        // 进行更新拼接
        List<RoleGroupDto> temp = new ArrayList<>(roleGroupRequest.getGroupIds().size());
        for (Integer groupId : roleGroupRequest.getGroupIds()) {
            Optional<RoleGroupDto> roleGroupDtoOptional = roleGroupList.stream().filter(t -> Objects.equals(groupId, t.getGroupId()) && Objects.equals(roleGroupRequest.getRoleId(), t.getRoleId())).findFirst();
            if (roleGroupDtoOptional.isPresent()) {
                RoleGroupDto roleGroupDto = roleGroupDtoOptional.get();
                roleGroupDto.setDeleteFlag(0);
                roleGroupDto.setUpdateUser(userCode);
                temp.add(roleGroupDto);
            } else {
                RoleGroupDto roleGroupDto = new RoleGroupDto();
                roleGroupDto.setCreateUser(userCode);
                roleGroupDto.setUpdateUser(userCode);
                roleGroupDto.setGroupId(groupId);
                roleGroupDto.setRoleId(roleGroupRequest.getRoleId());
                temp.add(roleGroupDto);
            }
        }
        // 进行批量更新
        boolean updateFlag = roleGroupService.saveBatch(temp);
        return updateFlag ? Result.successBuild() : Result.failBuild();
    }

}
