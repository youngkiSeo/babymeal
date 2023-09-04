package com.green.babymeal.mypage.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ProfileVo {
    private Long iuser;
    private String uid;
    private String address;
    private String addressDetail;
    private LocalDate birthday;
    private String image;
    private String mobileNb;
    private String unm;
    private String nickNm;
    private String zipcode;
    private int point;
    private List<BabyVo> baby;
}
