package com.green.babymeal.product.model;

import lombok.Data;

@Data
public class ProductVolumeDto {
    // 월별판매량용 dto
    private String date;
    private int saleVolume;
}
