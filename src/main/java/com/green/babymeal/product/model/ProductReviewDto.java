package com.green.babymeal.product.model;

import com.green.babymeal.common.entity.UserEntity;
import lombok.Data;

@Data
public class ProductReviewDto {
    private Long productId;
    private String ctnt;
}
