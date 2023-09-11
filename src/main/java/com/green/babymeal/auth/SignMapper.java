package com.green.babymeal.auth;


import com.green.babymeal.auth.model.SignIdDto;
import com.green.babymeal.auth.model.SignPwDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignMapper {
    String uidCheck(String uid);
    String selNickNm(String nickNm);

}
