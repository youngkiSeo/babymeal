package com.green.babymeal.auth.model;

import lombok.Data;

@Data
public class SignInReqDto {
    private String uid;
    private String upw;
}
