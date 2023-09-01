package com.green.babymeal.buy.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BuySelVo {
    private Long productId;
    private String name;
    private int count;
    private int price;
    private String thumbnail;
}
