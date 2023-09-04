package com.green.babymeal.baby.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
public class BabyInsVo {
    private LocalDate childBirth;
    private String prefer;
    private Long iuser;
    private String allegyId;
}
