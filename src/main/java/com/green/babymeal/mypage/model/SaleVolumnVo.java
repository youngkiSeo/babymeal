package com.green.babymeal.mypage.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SaleVolumnVo {
    private Long productId;
    private int count;
    private String pName;
    private int pPrice;

    public SaleVolumnVo(Long productId, int count, String pName, int pPrice) {
        this.productId = productId;
        this.count = count;
        this.pName = pName;
        this.pPrice = pPrice;
    }
}
