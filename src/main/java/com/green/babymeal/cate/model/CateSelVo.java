package com.green.babymeal.cate.model;

import lombok.Data;

@Data
public class CateSelVo {

    private Long productId;
    private String img;
    private int price;
    private String name;
    private int quantity;
    private int saleVoumn;

    public CateSelVo(Long productId, String img, int price, String name, int quantity, int saleVoumn) {
        this.productId = productId;
        this.img = img;
        this.price = price;
        this.name = name;
        this.quantity = quantity;
        this.saleVoumn = saleVoumn;
    }
}
