package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@Table(name = "review")
@NoArgsConstructor
@SuperBuilder
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