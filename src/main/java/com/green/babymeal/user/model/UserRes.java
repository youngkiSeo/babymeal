package com.green.babymeal.user.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRes {
    private Long iuser;
    private String email;
    private String password;
    private String name;
    private String mobileNb;
    //    private String role;
    private String zipCode;
    private String address;
    private String addressDetail;
    private String nickNm;
    private String birthday;
}
