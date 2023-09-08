package com.green.babymeal.mypage;

import com.green.babymeal.mypage.model.ProfileUpdPicDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MypageMapper {
    int patchProfile(ProfileUpdPicDto dto);
}
