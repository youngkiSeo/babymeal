package com.green.babymeal.baby.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class BabyInsVo {
    private LocalDate birthday;
    private String prefer;
    private Long iuser;
    private Long babyId;
    private Long allegyId;
}
