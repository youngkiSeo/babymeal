package com.green.babymeal.mypage.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ProfileVo {
    private Long iuser;
    private String address;
    private String addressDetail;
    private LocalDate birthday;
    private String email;
    private String image;
    private String mobileNb;
    private String name;
    private String nickNm;
    private String zipcode;
    private int point;
}
