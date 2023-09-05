package com.green.babymeal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonRes {
    SUCCESS(0, "가입에 성공하셨어요"), FAIL(-1, "가입에 실패하셨습니다.");

    int code;
    String msg;


}
