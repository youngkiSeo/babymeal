package com.green.babymeal.auth.model;

import lombok.Data;

@Data
public class SignUpReqDto {
    private String uid;
    private String upw;
    private String unm;
    private String email;
    private String address;
    private String addressDetail;
    private String birthday;
    private String mobileNb;
    private String nickNm;
}
