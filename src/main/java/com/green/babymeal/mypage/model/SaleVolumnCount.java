package com.green.babymeal.mypage.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class SaleVolumnCount {
    private Long productId;
    private int count;
}
