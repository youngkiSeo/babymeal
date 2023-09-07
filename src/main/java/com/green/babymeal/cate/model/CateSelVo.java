package com.green.babymeal.cate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CateSelVo {

    private Long productId;
    private String thumbnail;
    private int price;
    private String name;
    private int quantity;
    private int saleVoumn;
    private float pointRate;


    public CateSelVo(Long productId, String img, int price, String name, int quantity, int saleVoumn,float pointRate) {
        this.productId = productId;
        this.thumbnail = img;
        this.price = price;
        this.name = name;
        this.quantity = quantity;
        this.saleVoumn = saleVoumn;
        this.pointRate=pointRate;
    }
}
