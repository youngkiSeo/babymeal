package com.green.babymeal.mypage.model;

import com.green.babymeal.mypage.model.SaleVolumnCount;
import com.green.babymeal.mypage.model.SaleVolumnVo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SaleVolumnVoCount {
    private Long count;
    private int totalprice;
    private List<SaleVolumnVo> vo;
}
