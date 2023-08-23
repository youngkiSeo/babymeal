package com.green.babymeal.mypage.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.core.annotation.Order;

@Data
@Entity
@Order(3)
@Table(name = "orderdetail")
public class OrderDetailEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long orderDetailId;

    @ManyToOne
    @JoinColumn(name = "orderId",columnDefinition = "BIGINT UNSIGNED")
    private OrderDetailEntity orderId;

    @JoinColumn(name = "productId",columnDefinition = "BIGINT UNSIGNED")
    private Long productId;

    @Column(name = "count")
    private int count;

    @Column(name = "totalPrice",nullable = false)
    private int totalPrice;

    @ColumnDefault("0")
    @Column(name = "delYn")
    private int delYn;
}
