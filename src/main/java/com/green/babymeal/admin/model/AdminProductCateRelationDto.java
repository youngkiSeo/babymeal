package com.green.babymeal.admin.model;

import lombok.Data;

import java.util.List;

@Data
public class AdminProductCateRelationDto {

    private Long productId;
    private int cateId;
    private List<Integer> cateDetailId;

}
