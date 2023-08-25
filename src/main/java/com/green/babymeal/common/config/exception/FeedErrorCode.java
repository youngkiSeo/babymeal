package com.green.babymeal.common.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedErrorCode implements ErrorCode {
    NEED_FEED_IMGS(HttpStatus.BAD_REQUEST, "feed 이미지는 필수로 필요합니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
