package com.siliver.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siliver.admin.dao.UserGroupMapper;
import com.siliver.admin.dto.UserGroupDto;
import com.siliver.admin.service.IUserGroupService;
import org.springframework.stereotype.Service;

/**
 * 用户组用户服务实现
 *
 * @author siliver
 */
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroupDto> implements IUserGroupService {
}
