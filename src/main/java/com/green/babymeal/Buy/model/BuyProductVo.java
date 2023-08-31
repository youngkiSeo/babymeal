package com.green.babymeal.Buy.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Builder
public class BuyProductVo {
    private Long OrderId;
    private int totalprice;
    private int point;
    private int paymentprice;
}
