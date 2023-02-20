package com.siliver.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siliver.admin.common.Result;
import com.siliver.admin.dao.MenuMapper;
import com.siliver.admin.dto.MenuDto;
import com.siliver.admin.dto.RoleMenuDto;
import com.siliver.admin.neum.CommonValue;
import com.siliver.admin.request.MenuChangeRequest;
import com.siliver.admin.request.MenuRoleRequest;
import com.siliver.admin.response.MenuManageListResponse;
import com.siliver.admin.response.MenuRoleListResponse;
import com.siliver.admin.response.UserMenuResponse;
import com.siliver.admin.service.IMenuService;
import com.siliver.admin.service.IRoleMenuService;
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

/**
 * 菜单逻辑具体实现
 *
 * @author siliver
 */
@RequiredArgsConstructor
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuDto> implements IMenuService {

    private final CommonServiceImpl<MenuChangeRequest, MenuDto> commonService;

    private final IRoleMenuService roleMenuService;

    @Override
    public Result<List<UserMenuResponse>> getUserMenuListService(int userId) {
        List<UserMenuResponse> userMenuResponses = this.getBaseMapper().getUserMenuMapper(userId);
        return Result.successBuild(findChildNode(userMenuResponses, 0));
    }

    @Override
    public Result<IPage<MenuManageListResponse>> getMenuManageListService(int page, int pageSize, String groupCode) {
        LambdaQueryWrapper<MenuDto> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.hasText(groupCode)) {
            lambdaQueryWrapper.like(MenuDto::getMenuName, groupCode);
        }
        // 进行排序
        lambdaQueryWrapper.orderByDesc(MenuDto::getUpdateTime);
        // 进行字段查询拼接
        lambdaQueryWrapper.select(MenuDto::getId, MenuDto::getCreateTime, MenuDto::getUpdateTime, MenuDto::getCreateUser, MenuDto::getUpdateUser, MenuDto::getDeleteFlag, MenuDto::getMenuName, MenuDto::getMenuIcon, MenuDto::getMenuUrl);
        // 进行分页
        IPage<MenuDto> temp = new Page<>(page, pageSize);
        temp = page(temp, lambdaQueryWrapper);
        if (Objects.isNull(temp) || CollectionUtils.isEmpty(temp.getRecords())) {
            return Result.failBuild();
        }
        IPage<MenuManageListResponse> responseIpage = new Page<>();
        List<MenuManageListResponse> menuManageListResponses = new ArrayList<>(responseIpage.getRecords().size());
        temp.getRecords().forEach(t -> {
            MenuManageListResponse menuManageListResponse = new MenuManageListResponse();
            BeanUtils.copyProperties(t, menuManageListResponse);
            menuManageListResponses.add(menuManageListResponse);
        });
        responseIpage.setCurrent(temp.getCurrent());
        responseIpage.setTotal(temp.getTotal());
        responseIpage.setSize(temp.getSize());
        responseIpage.setRecords(menuManageListResponses);
        return Result.successBuild(responseIpage);
    }

    /**
     * 菜单管理变更接口
     *
     * @param change            操作类型
     * @param menuChangeRequest 变更参数
     * @return 变更结果
     */
    @Override
    public Result<Void> changeMenuManageService(String change, MenuChangeRequest menuChangeRequest) {
        // 进行非空判断
        if (CommonValue.UPDATE_STRING.equals(change)) {
            if (!StringUtils.hasText(menuChangeRequest.getMenuName())) {
                return Result.failBuild("菜单名称不能为空！");
            }
            if (1 > menuChangeRequest.getMenuName().length() || CODE_LENGTH < menuChangeRequest.getMenuName().length()) {
                return Result.failBuild("菜单名称长度为1-36！");
            }
            if (!StringUtils.hasText(menuChangeRequest.getMenuUrl())) {
                return Result.failBuild("用户组名称不能为空！");
            }
        }
        return commonService.changeDictionaryService(change, menuChangeRequest, new MenuDto());
    }

    /**
     * 菜单角色列表查询接口
     *
     * @param menuId 菜单编号
     * @return 菜单对应的角色列表
     */
    @Override
    public Result<List<MenuRoleListResponse>> getMenuRoleListService(int menuId) {
        // 获取角色列表
        List<MenuRoleListResponse> roleListResponses = baseMapper.getRolesByMenuIdMapper(menuId);
        if (CollectionUtils.isEmpty(roleListResponses)) {
            return Result.failBuild();
        }
        return Result.successBuild(roleListResponses);
    }

    /**
     * 菜单角色修改接口
     *
     * @param userCode        操作用户编号
     * @param menuRoleRequest 修改参数
     * @return 修改结果
     */
    @Override
    public Result<Void> changeMenuRoleService(String userCode, MenuRoleRequest menuRoleRequest) {
        // 进行关联查询
        List<RoleMenuDto> roleMenuList = roleMenuService.list(Wrappers.<RoleMenuDto>lambdaQuery().eq(RoleMenuDto::getMenuId, menuRoleRequest.getMenuId()).in(RoleMenuDto::getRoleId, menuRoleRequest.getRoleIds()).isNotNull(RoleMenuDto::getDeleteFlag));
        // 进行更新拼接
        List<RoleMenuDto> temp = new ArrayList<>(menuRoleRequest.getRoleIds().size());
        for (Integer roleId : menuRoleRequest.getRoleIds()) {
            Optional<RoleMenuDto> roleMenuDtoOptional = roleMenuList.stream().filter(t -> Objects.equals(roleId, t.getRoleId()) && Objects.equals(menuRoleRequest.getMenuId(), t.getMenuId())).findFirst();
            if (roleMenuDtoOptional.isPresent()) {
                RoleMenuDto roleMenuDto = roleMenuDtoOptional.get();
                roleMenuDto.setDeleteFlag(0);
                roleMenuDto.setUpdateUser(userCode);
                temp.add(roleMenuDto);
            } else {
                RoleMenuDto roleMenuDto = new RoleMenuDto();
                roleMenuDto.setCreateUser(userCode);
                roleMenuDto.setUpdateUser(userCode);
                roleMenuDto.setMenuId(menuRoleRequest.getMenuId());
                roleMenuDto.setRoleId(roleId);
                temp.add(roleMenuDto);
            }
        }
        // 进行批量更新
        boolean updateFlag = roleMenuService.saveBatch(temp);
        return updateFlag ? Result.successBuild() : Result.failBuild();
    }

    /**
     * 进行节点树的拼接
     *
     * @param menuResponses 菜单列表
     * @return 用户组结构
     */
    private List<UserMenuResponse> findChildNode(List<UserMenuResponse> menuResponses, int begin) {
        // 进行菜单结构的拼接
        return menuResponses.stream().filter(t -> begin == t.getParentId()).peek(t -> {
            List<UserMenuResponse> userMenuResponses = menuResponses.stream().filter(t1 -> t1.getParentId() == t.getId()).toList();
            t.setChild(findChildNode(userMenuResponses, t.getId()));
        }).toList();
    }

}
