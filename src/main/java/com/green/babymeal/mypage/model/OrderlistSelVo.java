package com.green.babymeal.mypage.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderlistSelVo {
    private Long orderId;
    private Long orderCode;
    private LocalDate createdAt;
    private int totalprice;
    private String pName;
    private String img;
    private Byte shipment;

    public OrderlistSelVo(Long orderId, Long orderCode, LocalDate createdAt, int totalprice, String pName, String img, Byte shipment) {
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.createdAt = createdAt;
        this.totalprice = totalprice;
        this.pName = pName;
        this.img = img;
        this.shipment = shipment;
    }
}
