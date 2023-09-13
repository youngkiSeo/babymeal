package com.green.babymeal.email;

import com.green.babymeal.auth.model.SignIdDto;
import com.green.babymeal.auth.model.SignPwDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface EmailMapper {
    SignPwDto findPassword(String mail);

    void updPassword(Long iuser, String pw);
    String emailCheck(String email);
    String SelNickNm(String nickNm);

    SignIdDto findUserId(String mobileNb);
}
