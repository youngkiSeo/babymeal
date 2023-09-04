package com.green.babymeal.auth.model;

import lombok.Data;

@Data
public class SignPwDto {
    private String mail;
    private String mobileNb;
    private Long iuser;
}
