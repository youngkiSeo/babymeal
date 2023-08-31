package com.green.babymeal.mypage.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SaleVolumnVo {
    //private Long salevolumnId;
    //private LocalDateTime createdAt;
    private Long count;
    private Long productId;
    private String pName;
    private int price;

    public SaleVolumnVo(Long count, Long productId, String pName, int price) {
        this.count = count;
        this.productId = productId;
        this.pName = pName;
        this.price = price;
    }
}
