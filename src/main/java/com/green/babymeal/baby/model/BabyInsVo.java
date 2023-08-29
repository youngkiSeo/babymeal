package com.green.babymeal.baby.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BabyInsVo {
    private String birthday;
    private String prefer;
    private Long iuser;
    private Long babyId;
    private Long allegyId;
}
