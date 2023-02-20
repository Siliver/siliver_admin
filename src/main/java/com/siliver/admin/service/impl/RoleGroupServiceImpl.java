package com.siliver.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siliver.admin.dao.RoleGroupMapper;
import com.siliver.admin.dto.RoleGroupDto;
import com.siliver.admin.service.IRoleGroupService;
import org.springframework.stereotype.Service;

/**
 * 角色分组服务实现
 *
 * @author siliver
 */
@Service
public class RoleGroupServiceImpl extends ServiceImpl<RoleGroupMapper, RoleGroupDto> implements IRoleGroupService {
}
