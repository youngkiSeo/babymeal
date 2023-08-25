package com.green.babymeal.common.config.security.model;

import lombok.Data;

@Data
public class UserLoginDto {
    private String email;
    private String password;
}
