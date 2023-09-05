package com.green.babymeal.baby.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BabyUpdDto {
    private Long BabyId;
    private LocalDate childBirth;
    private String prefer;
    private String allergyId;
}
