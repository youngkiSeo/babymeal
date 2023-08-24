package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.core.annotation.Order;

@Data
@Entity
@Order(4)
@Table(name = "orderdetail")
public class OrderDetailEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED",length=20)
    private Long orderDetailId;

    @ManyToOne
    @JoinColumn(name = "orderId",columnDefinition = "BIGINT UNSIGNED")
    private OrderDetailEntity orderId;

    @JoinColumn(name = "productId",columnDefinition = "BIGINT UNSIGNED")
    private Long productId;

    @Column(name = "count",length=11)
    private int count;

    @Column(name = "totalPrice",nullable = false,length=11)
    private int totalPrice;

    @ColumnDefault("0")
    @Column(name = "delYn",length=4)
    private Byte delYn;
}
