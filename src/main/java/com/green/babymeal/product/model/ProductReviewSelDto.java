package com.green.babymeal.product.model;

import com.green.babymeal.common.entity.ProductEntity;
import com.green.babymeal.common.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductReviewSelDto {
    private Long reviewId;
    private Long iuser;
    //private ProductEntity productId;
    private String ctnt;
    private String userName;
}
