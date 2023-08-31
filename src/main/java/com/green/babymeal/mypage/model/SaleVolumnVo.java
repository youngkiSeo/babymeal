package com.green.babymeal.mypage.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaleVolumnVo {
    //private Long salevolumnId;
    //private LocalDateTime createdAt;
    private Long count;
    private Long productId;
    private String pName;

    public SaleVolumnVo(Long count, Long productId, String pName) {
        this.count = count;
        this.productId = productId;
        this.pName = pName;
    }
}
