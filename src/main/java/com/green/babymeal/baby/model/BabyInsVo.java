package com.green.babymeal.baby.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class BabyInsVo {
    private String birthday;
    private String prefer;
    private Long iuser;
    private Long babyId;
    private String allegyId;
}
