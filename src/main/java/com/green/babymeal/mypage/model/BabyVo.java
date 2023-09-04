package com.green.babymeal.mypage.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class BabyVo {
    private Long babyId;
    private LocalDate childBirth;
    private List<String> allergyname;

}
