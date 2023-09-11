package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "productImage")
public class ProductImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imgId", updatable = false,nullable = false, columnDefinition = "BIGINT UNSIGNED") //
    private Long p_img_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    @ToString.Exclude
    private ProductEntity productId;

    @Column
    private String img;

}
