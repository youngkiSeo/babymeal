package com.green.babymeal.mypage.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class SaleVolumnColorVo {
    private Long productId;
    private  String id;
    private  String label;
    private  int value;
    private String color;


}
