package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.catalina.User;
import org.springframework.core.annotation.Order;

@Entity
@Data
@Order(2)
@Table(name = "review")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "iuser")
    private UserEntity iuser;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductEntity product_id;

    @Column
    private String ctnt;
}
