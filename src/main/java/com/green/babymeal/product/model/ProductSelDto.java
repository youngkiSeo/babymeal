package com.green.babymeal.product.model;

import lombok.Data;

import java.util.List;

@Data
public class ProductSelDto {
    private String pName;
    private String description;
    private int pPrice;
    private int pQuantity;
    private int saleVoumn;
    private Long cateId;
    private List<String> allergyNames;
    private List<String> thumbnail;
}
