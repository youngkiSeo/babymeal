package com.green.babymeal.mypage.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class OrderlistStrVo {
    private Long orderId;
    private Long orderCode;
    private LocalDate createdAt;
    private int totalprice;
    private String pName;
    private String img;
    private String shipment;
}
