package com.green.babymeal.auth.model;

import lombok.Data;

@Data
public class SignUpResultDto {
    private boolean success;
    private int code;
    private String msg;
}
