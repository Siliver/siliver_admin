package com.siliver.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.siliver.admin.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserDto> {
}
