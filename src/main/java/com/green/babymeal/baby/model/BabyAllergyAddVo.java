package com.green.babymeal.baby.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BabyAllergyAddVo {
    private Long allergyId;
    private Long babyId;
}
