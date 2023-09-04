package com.green.babymeal.auth;


import com.green.babymeal.auth.model.SignIdDto;
import com.green.babymeal.auth.model.SignPwDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignMapper {
    SignPwDto findPassword(String mail);

    void updPassword(Long iuser, String pw);
    String uidCheck(String uid);
    String selNickNm(String nickNm);

    SignIdDto findUserId(String mobileNb);
}
