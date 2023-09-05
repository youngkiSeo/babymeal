package com.green.babymeal.baby.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BabyInsDto {
    private LocalDate childBirth;
    private String prefer;
//    private Long iuser;
    private String allergyId;

}
