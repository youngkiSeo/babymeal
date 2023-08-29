package com.green.babymeal.user;

import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.user.model.UserDelDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int delUser(UserDelDto dto);
}
