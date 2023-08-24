package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.annotation.Order;

@Entity
@Data
@Table(name = "product_category_relation")
@Order(99)
public class CateRelationEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_cate_id",columnDefinition = "BIGINT UNSIGNED")
    private Long productCateId;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private ProductEntity productEntity;

    @ManyToOne
    @JoinColumn(name = "cateId",nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private CategoryEntity categoryEntity;

    @ManyToOne
    @JoinColumn(name = "cateDetailId",nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private CateDetailEntity cateDetailEntity;


}
