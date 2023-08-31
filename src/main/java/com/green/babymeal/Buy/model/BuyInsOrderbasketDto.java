package com.green.babymeal.Buy.model;

import lombok.Data;

@Data
public class BuyInsOrderbasketDto {
    private Long cartId;
    private Long productId;
    private int count;
    private int totalprice;
}
