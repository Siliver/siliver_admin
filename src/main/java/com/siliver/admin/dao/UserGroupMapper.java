package com.siliver.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.siliver.admin.dto.UserGroupDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户组用户mapper
 *
 * @author siliver
 */
@Mapper
public interface UserGroupMapper extends BaseMapper<UserGroupDto> {

}
