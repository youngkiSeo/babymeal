package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Data
@Table(name = "order_basket")
public class OrderBasketEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long cart_id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @ManyToOne
    @JoinColumn(name = "iuser")
    private UserEntity userEntity;

    private int count;

    @Column(name = "created_at")
    private LocalDate createAt;

}
