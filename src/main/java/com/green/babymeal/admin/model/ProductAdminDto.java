package com.green.babymeal.admin.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ProductAdminDto {
    // 관리자 - 상품조회용 데이터
    //알러지, 카테, 서브카테, 이름, 가격, 썸네일, 본문이미지 (이미지전부), 본문내용, 상품 pk, 재고
    private Long productId;
    private String name;
    private int price;
    private int quantity;
    private Long cate; // 1차카테
    private List<Long> cateDetail; // 2차카테
    private List<Long> allegyName; // 알러지
    private String thumbnail; // 썸네일

}
