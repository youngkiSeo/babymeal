package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product_category_relation")
public class CateRelationEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_cate_id")
    private Long productCateId;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private ProductEntity productId;

    @ManyToOne
    @JoinColumn(name = "cate_id",nullable = false)
    private CategoryEntity cateId;

    @ManyToOne
    @JoinColumn(name = "cate_detail_id",nullable = false)
    private CateDetailEntity cateDetailId;


}
