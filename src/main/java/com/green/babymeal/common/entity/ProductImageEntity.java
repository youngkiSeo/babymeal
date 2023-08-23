package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "productImage")
public class ProductImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imgId", updatable = false,nullable = false, columnDefinition = "BIGINT UNSIGNED") //
    private Long p_img_id;

    @OneToOne
    @JoinColumn(name = "product_id",nullable = false)
    private ProductEntity productId;

    @Column
    private String img;

}
