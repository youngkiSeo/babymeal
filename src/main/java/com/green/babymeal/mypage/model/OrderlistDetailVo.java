package com.green.babymeal.mypage.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OrderlistDetailVo {
    private Long productId;
    private String img;
    private String pName;
    private LocalDate createdAt;
    private int price;
    private int count;
    private int totalPrice;

    public OrderlistDetailVo(Long productId, String img, String pName, LocalDate createdAt, int price, int count, int totalPrice) {
        this.productId = productId;
        this.img = img;
        this.pName = pName;
        this.createdAt = createdAt;
        this.price = price;
        this.count = count;
        this.totalPrice = totalPrice;
    }
}
