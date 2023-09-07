package com.green.babymeal.admin.model;

import lombok.Data;

import java.util.List;

@Data
public class AdminProductUpdDto {
    Long productId;
    String name;
    int price;
    int quantity;
    String description;
    int saleVolume;
    int category;
    float pointRate;
    List<Long> allergyId;
    List<Integer> cateDetail;
}
