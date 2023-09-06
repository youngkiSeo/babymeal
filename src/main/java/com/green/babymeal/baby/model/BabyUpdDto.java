package com.green.babymeal.baby.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BabyUpdDto {
    private Long BabyId;
    private LocalDate childBirth;
    private String prefer;
    private String allergyId;
    private List<Long> allergyList;
}
