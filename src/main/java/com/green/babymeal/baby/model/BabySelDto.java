package com.green.babymeal.baby.model;

import lombok.Builder;
import lombok.Data;

@Data
public class BabySelDto {
    private Long babyId;
    private Long iuser;
    private String childBirth;
    private String prefer;

}
