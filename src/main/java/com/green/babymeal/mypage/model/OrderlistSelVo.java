package com.green.babymeal.mypage.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderlistSelVo {
    private Long orderId;
    private LocalDateTime createdAt;
    private int price;
    private String thumbnail;
    private String name;

    private int shipment;
}
