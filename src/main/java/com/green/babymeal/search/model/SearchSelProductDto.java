package com.green.babymeal.search.model;

import lombok.Data;

@Data
public class SearchSelProductDto {
    private int productid;
    private String name;
    private String img;
    private int price;
    private String cateId;
}
