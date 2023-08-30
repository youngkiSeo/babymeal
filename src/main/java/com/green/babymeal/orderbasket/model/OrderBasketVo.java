package com.green.babymeal.orderbasket.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class OrderBasketVo {

    private Long cartId;
    private Long productId;
    private String productName;
    private int count;
    private int price;
    private String thumbnail;
    private LocalDate createdAt;
}
