package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "review")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "iuser")
    private UserEntity iuser;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productId;

    @Column
    private String ctnt;
}