package com.green.babymeal.admin.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductAdminSelDto {
    private Long productId;
    private String name;
    private int price;
    private Long cate; // 1차카테
    private List<Long> cateDetail; // 2차카테
    private List<Long> allegyId; // 알러지
    private List<String> thumbnail; // 썸네일
    private String description; // 본문 (본문이미지 포함된 태그임)
    private int quantity; // 재고
}
