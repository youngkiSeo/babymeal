package com.green.babymeal.user.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInsDto {
    private String address;
    private String address_detail;
    private String birthday;
    private String email;
    private String mobile_nb;
    private String name;
    private String nick_name;
    private String password;
    private String uid;
    private String zip_code;
}
