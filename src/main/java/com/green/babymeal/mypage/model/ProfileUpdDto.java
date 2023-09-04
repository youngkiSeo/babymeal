package com.green.babymeal.mypage.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileUpdDto {
    private String nickNm;
    private String upw;
    private String phoneNumber;
    private String umn;
    private LocalDate birthday;
    private String zipcode;
    private String address;
    private String addressDetail;
}
