package com.green.babymeal.baby.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BabyInsDto {
    private String birthday;
    private String prefer;
    private Long iuser;
    private Long allegyId;

}
