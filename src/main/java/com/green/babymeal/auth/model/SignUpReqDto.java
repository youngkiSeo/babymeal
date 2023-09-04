package com.green.babymeal.auth.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpReqDto {
    private String uid;
    private String upw;
    private String unm;
//    private String email;
    private String address;
    private String addressDetail;
    private LocalDate birthday;
    private String mobileNb;
    private String nickNm;
    private String zipCode;
}
