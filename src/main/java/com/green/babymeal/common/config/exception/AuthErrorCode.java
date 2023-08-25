package com.green.babymeal.common.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "refresh token이 없습니다."),
    NOT_FOUND_ID(HttpStatus.NOT_FOUND, "아이디를 확인해 주세요."),
    VALID_PW(HttpStatus.NOT_FOUND, "비밀번호를 확인해 주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
