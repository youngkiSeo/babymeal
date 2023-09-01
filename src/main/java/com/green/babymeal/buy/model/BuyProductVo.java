package com.green.babymeal.buy.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BuyProductVo {
    private Long OrderId;
    private Long OrderCode;
    private int totalprice;
    private int point;
    private int paymentprice;
}
